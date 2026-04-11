package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.*;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentProcessing;
import io.github.hirannor.hexadocs.application.document.usecase.StartDocumentProcessing;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class DocumentProcessingService implements DocumentProcessing {

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

        try {

            final byte[] file = documentStorage.load(documentId);

            if (file == null || file.length == 0) {
                throw new IllegalStateException("Document file not found: " + documentId);
            }

            final String text = textExtractor.extract(file);

            if (text == null || text.isBlank()) {
                throw new IllegalStateException("Extracted text is empty for document: " + documentId);
            }

            documentTextStorage.save(documentId, text);

            final List<Chunk> chunks = textChunker.chunk(text);

            if (chunks == null || chunks.isEmpty()) {
                throw new IllegalStateException("No chunks generated for document: " + documentId);
            }

            final List<VectorDocument> vectorDocuments = new ArrayList<>();

            int index = 0;
            for (Chunk chunk : chunks) {

                Map<String, Object> metadata = new HashMap<>();
                metadata.put("chunkIndex", index++);
                metadata.put("documentId", documentId.asText());
                metadata.put("knowledgeBaseId", knowledgeBaseId.asText());

                vectorDocuments.add(new VectorDocument(
                        UUID.randomUUID().toString(),
                        documentId,
                        knowledgeBaseId,
                        chunk.content(),
                        metadata
                ));
            }
            knowledgeStore.store(vectorDocuments);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Document processing failed for documentId=" + documentId,
                    e
            );
        }
    }
}