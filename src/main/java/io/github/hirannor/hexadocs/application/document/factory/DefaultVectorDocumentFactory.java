package io.github.hirannor.hexadocs.application.document.factory;

import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.VectorDocument;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class DefaultVectorDocumentFactory implements VectorDocumentFactory {

    private static final String CHUNK_INDEX = "chunkIndex";
    private static final String DOCUMENT_ID = "documentId";
    private static final String KNOWLEDGE_BASE_ID = "knowledgeBaseId";

    @Override
    public List<VectorDocument> create(
            final List<Chunk> chunks,
            final DocumentId documentId,
            final KnowledgeBaseId knowledgeBaseId
    ) {
        final List<VectorDocument> result = new ArrayList<>();

        int index = 0;

        for (final Chunk chunk : chunks) {
            final Map<String, Object> metadata = new HashMap<>();
            metadata.put(CHUNK_INDEX, index++);
            metadata.put(DOCUMENT_ID, documentId.asText());
            metadata.put(KNOWLEDGE_BASE_ID, knowledgeBaseId.asText());

            result.add(
                    VectorDocument.empty()
                            .id(UUID.randomUUID().toString())
                            .documentId(documentId)
                            .knowledgeBaseId(knowledgeBaseId)
                            .content(chunk.content())
                            .metadata(metadata)
                            .assemble()
            );
        }

        return result;
    }
}