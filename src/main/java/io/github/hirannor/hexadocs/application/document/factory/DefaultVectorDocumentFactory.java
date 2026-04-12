package io.github.hirannor.hexadocs.application.document.factory;

import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.VectorDocument;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class DefaultVectorDocumentFactory implements VectorDocumentFactory {

    @Override
    public List<VectorDocument> create(
            final List<Chunk> chunks,
            final DocumentId documentId,
            final KnowledgeBaseId knowledgeBaseId
    ) {
        final List<VectorDocument> result = new ArrayList<>();

        for (final Chunk chunk : chunks) {
            result.add(
                    VectorDocument.empty()
                            .id(UUID.randomUUID().toString())
                            .documentId(documentId)
                            .knowledgeBaseId(knowledgeBaseId)
                            .content(chunk.content())
                            .assemble()
            );
        }

        return result;
    }
}