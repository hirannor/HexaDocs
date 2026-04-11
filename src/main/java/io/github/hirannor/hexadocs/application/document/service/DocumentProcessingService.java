package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.factory.VectorDocumentFactory;
import io.github.hirannor.hexadocs.application.document.port.*;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentProcessing;
import io.github.hirannor.hexadocs.application.document.usecase.StartDocumentProcessing;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.events.DocumentProcessed;
import io.github.hirannor.hexadocs.domain.document.events.DocumentProcessingFailed;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DocumentProcessingService implements DocumentProcessing {

    private static final Logger log = LoggerFactory.getLogger(DocumentProcessingService.class);

    private final VectorDocumentFactory vectors;
    private final DocumentStorage documentStorage;
    private final TextChunker textChunker;
    private final DocumentTextStorage documentTextStorage;
    private final KnowledgeStore knowledgeStore;
    private final TextExtractor textExtractor;
    private final MessagePublisher messages;

    DocumentProcessingService(final VectorDocumentFactory vectors,
                              final DocumentStorage documentStorage,
                              final TextChunker textChunker,
                              final DocumentTextStorage documentTextStorage,
                              final KnowledgeStore knowledgeStore,
                              final TextExtractor textExtractor,
                              final MessagePublisher messages) {
        this.vectors = vectors;
        this.documentStorage = documentStorage;
        this.textChunker = textChunker;
        this.documentTextStorage = documentTextStorage;
        this.knowledgeStore = knowledgeStore;
        this.textExtractor = textExtractor;
        this.messages = messages;
    }

    @Override
    public void process(final StartDocumentProcessing command) {
        final DocumentId documentId = command.documentId();
        final KnowledgeBaseId knowledgeBaseId = command.knowledgeBaseId();

        log.info("Starting document processing | jobId={} | documentId={} | knowledgeBaseId={}",
                command.jobId().asText(),
                documentId.asText(),
                knowledgeBaseId.asText());

        try {
            final byte[] rawDocument = documentStorage.loadById(documentId);

            if (rawDocument == null || rawDocument.length == 0) {
                throw new IllegalStateException("Document not found: " + documentId);
            }

            final String extractedText = textExtractor.extract(rawDocument);

            if (extractedText == null || extractedText.isBlank()) {
                throw new IllegalStateException("Extracted text is empty for document: " + documentId);
            }

            log.info("Text extracted successfully | documentId={} | length={}",
                    documentId.asText(), extractedText.length());

            documentTextStorage.save(documentId, extractedText);

            final List<Chunk> chunks = textChunker.chunk(extractedText);

            if (chunks == null || chunks.isEmpty()) {
                throw new IllegalStateException("No chunks generated for document: " + documentId);
            }

            log.info("Chunks generated | documentId={} | chunkCount={}",
                    documentId.asText(), chunks.size());

            final List<VectorDocument> documents = vectors.create(chunks, documentId, knowledgeBaseId);

            knowledgeStore.store(documents);

            log.info("Document processing completed successfully | jobId={} | documentId={}",
                    command.jobId().asText(), documentId.asText());

            messages.publish(
                    DocumentProcessed.record(
                            command.jobId(),
                            documentId,
                            knowledgeBaseId
                    )
            );
        } catch (Exception e) {
            log.error("Document processing failed | jobId={} | documentId={} | error={}",
                    command.jobId().asText(),
                    documentId.asText(),
                    e.getMessage(),
                    e);

            messages.publish(
                    DocumentProcessingFailed.record(
                            command.jobId(),
                            documentId,
                            knowledgeBaseId,
                            e.getMessage()
                    )
            );

            throw e;
        }
    }
}