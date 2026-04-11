package io.github.hirannor.hexadocs.application.document.port;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.Map;

public record VectorDocument(
        String id,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        String content,
        Map<String, Object> metadata
) {
}

