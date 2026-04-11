package io.github.hirannor.hexadocs.application.document.factory;

import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.VectorDocument;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;

public interface VectorDocumentFactory {
    List<VectorDocument> create(
            final List<Chunk> chunks,
            final DocumentId documentId,
            final KnowledgeBaseId knowledgeBaseId
    );
}