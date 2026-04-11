package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.factory.VectorDocumentFactory;
import io.github.hirannor.hexadocs.application.document.port.*;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentVectorIndexed;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentVectorIndexing;
import io.github.hirannor.hexadocs.application.document.usecase.IndexDocument;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DocumentVectorIndexingService implements DocumentVectorIndexing {

    private static final Logger log =
            LoggerFactory.getLogger(DocumentVectorIndexingService.class);

    private final VectorDocumentFactory vectors;
    private final TextChunker textChunker;
    private final DocumentTextStorage documentTextStorage;
    private final KnowledgeStore knowledgeStore;
    private final MessagePublisher messages;

    DocumentVectorIndexingService(final VectorDocumentFactory vectors,
                                  final TextChunker textChunker,
                                  final DocumentTextStorage documentTextStorage,
                                  final KnowledgeStore knowledgeStore,
                                  final MessagePublisher messages) {
        this.vectors = vectors;
        this.textChunker = textChunker;
        this.documentTextStorage = documentTextStorage;
        this.knowledgeStore = knowledgeStore;
        this.messages = messages;
    }

    @Override
    public void index(final IndexDocument command) {

        final DocumentId documentId = command.documentId();
        final KnowledgeBaseId knowledgeBaseId = command.knowledgeBaseId();

        log.info("Starting vector indexing | documentId={} | knowledgeBaseId={}",
                documentId.asText(),
                knowledgeBaseId.asText());

            final String extractedText =
                    documentTextStorage.load(documentId)
                    .orElseThrow(() -> new IllegalStateException(
                    "Extracted text not found for documentId: " + documentId
            ));


            final List<Chunk> chunks = textChunker.chunk(extractedText);

            if (chunks == null || chunks.isEmpty()) {
                throw new IllegalStateException(
                    "No chunks generated for documentId: " + documentId
                );
            }

            log.info("Chunks created | documentId={} | count={}",
                    documentId.asText(), chunks.size());

            final List<VectorDocument> vectors =
                    this.vectors.create(chunks, documentId, knowledgeBaseId);

            knowledgeStore.store(vectors);

            log.info("Vector indexing completed | documentId={} | vectors={}",
                    documentId.asText(), vectors.size());

            messages.publish(
                    DocumentVectorIndexed.record(
                            command.ingestionJobId(),
                            documentId,
                            knowledgeBaseId,
                            vectors.size()
                    )
            );

    }
}