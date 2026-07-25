package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.chunking.Chunk;
import io.github.hirannor.hexadocs.application.document.port.chunking.ChunkText;
import io.github.hirannor.hexadocs.application.document.port.chunking.TextChunker;
import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractedPage;
import io.github.hirannor.hexadocs.application.document.port.storage.documenttext.DocumentTextStorage;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorDocument;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorDocumentFactory;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentVectorIndexed;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentVectorIndexing;
import io.github.hirannor.hexadocs.application.document.usecase.IndexDocument;
import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import io.github.hirannor.hexadocs.domain.document.DocumentMetadataRepository;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class DocumentVectorIndexingService implements DocumentVectorIndexing {

    private static final int BATCH_SIZE = 20;
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentVectorIndexingService.class);

    private final VectorDocumentFactory vectors;
    private final TextChunker textChunker;
    private final DocumentTextStorage documentTextStorage;
    private final DocumentMetadataRepository documents;
    private final KnowledgeStore knowledgeStore;
    private final MessagePublisher messages;

    DocumentVectorIndexingService(final VectorDocumentFactory vectors, final TextChunker textChunker,
                                  final DocumentTextStorage documentTextStorage,
                                  final DocumentMetadataRepository documents, final KnowledgeStore knowledgeStore,
                                  final MessagePublisher messages) {
        this.vectors = vectors;
        this.textChunker = textChunker;
        this.documentTextStorage = documentTextStorage;
        this.documents = documents;
        this.knowledgeStore = knowledgeStore;
        this.messages = messages;
    }

    @Override
    public void index(final IndexDocument command) {

        final DocumentId documentId = command.documentId();

        final KnowledgeBaseId knowledgeBaseId = command.knowledgeBaseId();

        try {

            LOGGER.info("Starting vector indexing | documentId={}", documentId.asText());

            final Document document = documents.findById(documentId)
                    .orElseThrow(() -> new IllegalStateException("Document not found for documentId: " + documentId));

            final List<ExtractedPage> pages = documentTextStorage.load(documentId);

            if (pages.isEmpty()) {
                throw new IllegalStateException("Extracted text not found for documentId: " + documentId);
            }

            LOGGER.info("Extracted pages loaded | documentId={} | pageCount={}", documentId.asText(), pages.size());

            final List<Chunk> chunks = chunkDocument(pages, documentId, document.language());

            if (chunks.isEmpty()) {
                throw new IllegalStateException("No chunks generated");
            }

            LOGGER.info("Chunks created | documentId={} | count={}", documentId.asText(), chunks.size());

            final List<List<Chunk>> batches = batch(chunks, BATCH_SIZE);

            int totalStored = 0;

            for (final List<Chunk> batch : batches) {

                final List<VectorDocument> vectorDocuments = vectors.create(batch, documentId, knowledgeBaseId);

                knowledgeStore.store(vectorDocuments);

                totalStored += vectorDocuments.size();
            }

            LOGGER.info("Vector indexing completed | documentId={} | storedVectors={}", documentId.asText(),
                    totalStored);

            messages.publish(
                    DocumentVectorIndexed.record(command.ingestionJobId(), documentId, knowledgeBaseId, totalStored));

        } catch (final Exception e) {

            LOGGER.error("Vector indexing failed | documentId={}", documentId.asText(), e);

            messages.publish(DocumentVectorIndexingFailed.record(command.ingestionJobId(), e.getMessage()));
        }
    }

    private List<Chunk> chunkDocument(final List<ExtractedPage> pages, final DocumentId documentId,
                                      final DocumentLanguage language) {
        final List<Chunk> chunks = new ArrayList<>();

        int order = 0;

        for (final ExtractedPage page : pages) {

            if (page.text() == null || page.text().isBlank()) {
                continue;
            }

            final List<Chunk> pageChunks = textChunker.chunk(
                    ChunkText.issue(page.text(), documentId, language, page.pageNumber(), page.extractionMethod()));

            for (final Chunk pageChunk : pageChunks) {

                chunks.add(Chunk.of(pageChunk.documentId(), page.pageNumber(), pageChunk.content(), order++,
                        pageChunk.extractionMethod()));
            }
        }

        return chunks;
    }

    private List<List<Chunk>> batch(final List<Chunk> chunks, final int size) {
        final List<List<Chunk>> batches = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i += size) {
            batches.add(chunks.subList(i, Math.min(i + size, chunks.size())));
        }

        return batches;
    }
}
