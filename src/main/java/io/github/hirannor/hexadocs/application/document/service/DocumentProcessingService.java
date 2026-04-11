package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.*;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentProcessing;
import io.github.hirannor.hexadocs.application.document.usecase.StartDocumentProcessing;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class DocumentProcessingService implements DocumentProcessing {

    private static final Logger log = LoggerFactory.getLogger(DocumentProcessingService.class);

    private static final String CHUNK_INDEX = "chunkIndex";
    private static final String DOCUMENT_ID = "documentId";
    private static final String KNOWLEDGE_BASE_ID = "knowledgeBaseId";

    private final DocumentStorage documentStorage;
    private final TextChunker textChunker;
    private final DocumentTextStorage documentTextStorage;
    private final KnowledgeStore knowledgeStore;
    private final TextExtractor textExtractor;

    DocumentProcessingService(final DocumentStorage documentStorage,
                              final TextChunker textChunker,
                              final DocumentTextStorage documentTextStorage,
                              final KnowledgeStore knowledgeStore,
                              final TextExtractor textExtractor) {
        this.documentStorage = documentStorage;
        this.textChunker = textChunker;
        this.documentTextStorage = documentTextStorage;
        this.knowledgeStore = knowledgeStore;
        this.textExtractor = textExtractor;
    }

    @Override
    public void process(final StartDocumentProcessing command) {
        final DocumentId documentId = command.documentId();
        final KnowledgeBaseId knowledgeBaseId = command.knowledgeBaseId();

        log.info("Starting document processing | documentId={} | knowledgeBaseId={}",
                documentId.asText(), knowledgeBaseId.asText());

        final byte[] rawDocument = documentStorage.loadById(documentId);

        if (rawDocument == null || rawDocument.length == 0) {
            log.warn("Document not found or empty | documentId={}", documentId.asText());
            throw new IllegalStateException("Document not found: " + documentId);
        }

        final String extractedText = textExtractor.extract(rawDocument);

        if (extractedText == null || extractedText.isBlank()) {
            log.warn("Extracted text is empty | documentId={}", documentId.asText());
            throw new IllegalStateException("Extracted text is empty for document: " + documentId);
        }

        log.info("Text extracted successfully | documentId={} | length={}",
                documentId.asText(), extractedText.length());

        documentTextStorage.save(documentId, extractedText);

        final List<Chunk> chunks = textChunker.chunk(extractedText);

        if (chunks == null || chunks.isEmpty()) {
            log.warn("No chunks generated | documentId={}", documentId.asText());
            throw new IllegalStateException("No chunks generated for document: " + documentId);
        }

        log.info("Chunks generated | documentId={} | chunkCount={}",
                documentId.asText(), chunks.size());

        final List<VectorDocument> vectors =
                buildVectorDocuments(chunks, documentId, knowledgeBaseId);

        knowledgeStore.store(vectors);

        log.info("Document processing completed successfully | documentId={}", documentId.asText());
    }

    private List<VectorDocument> buildVectorDocuments(
            final List<Chunk> chunks,
            final DocumentId documentId,
            final KnowledgeBaseId knowledgeBaseId
    ) {
        final List<VectorDocument> vectorDocuments = new ArrayList<>();

        int index = 0;

        for (final Chunk chunk : chunks) {

            final Map<String, Object> metadata = new HashMap<>();
            metadata.put(CHUNK_INDEX, index++);
            metadata.put(DOCUMENT_ID, documentId.asText());
            metadata.put(KNOWLEDGE_BASE_ID, knowledgeBaseId.asText());

            final VectorDocument vector = VectorDocument.empty()
                    .id(UUID.randomUUID().toString())
                    .documentId(documentId)
                    .knowledgeBaseId(knowledgeBaseId)
                    .content(chunk.content())
                    .metadata(metadata)
                    .assemble();

            vectorDocuments.add(vector);
        }

        return vectorDocuments;
    }
}