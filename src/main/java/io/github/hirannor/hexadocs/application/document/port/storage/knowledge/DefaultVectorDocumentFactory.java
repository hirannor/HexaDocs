package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import io.github.hirannor.hexadocs.application.document.port.chunking.Chunk;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class DefaultVectorDocumentFactory implements VectorDocumentFactory {

    @Override
    public List<VectorDocument> create(final List<Chunk> chunks, final DocumentId documentId,
                                       final KnowledgeBaseId knowledgeBaseId) {
        final List<VectorDocument> result = new ArrayList<>(chunks.size());

        for (final Chunk chunk : chunks) {
            result.add(
                    VectorDocument.empty().chunkId(chunk.id()).chunkHash(chunk.contentHash()).chunkOrder(chunk.order())
                            .documentId(documentId).knowledgeBaseId(knowledgeBaseId).content(chunk.content())
                            .metadata("pageNumber", chunk.pageNumber()).metadata("chunkOrder", chunk.order())
                            .metadata("extractionMethod", chunk.extractionMethod().name()).assemble());
        }

        return result;
    }
}
