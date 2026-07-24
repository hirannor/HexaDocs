package io.github.hirannor.hexadocs.application.document.port.storage.content;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;

public interface DocumentContentReader {
    List<DocumentChunk> readAll(KnowledgeBaseId knowledgeBaseId);
}
