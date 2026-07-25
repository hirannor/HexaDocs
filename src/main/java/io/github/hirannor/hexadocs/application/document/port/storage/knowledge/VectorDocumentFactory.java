package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import io.github.hirannor.hexadocs.application.document.port.chunking.Chunk;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;

public interface VectorDocumentFactory {
    List<VectorDocument> create(final List<Chunk> chunks, final DocumentId documentId,
                                final KnowledgeBaseId knowledgeBaseId);
}