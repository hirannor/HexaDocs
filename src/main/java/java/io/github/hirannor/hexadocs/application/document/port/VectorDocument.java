package java.io.github.hirannor.hexadocs.application.document.port;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.util.List;
import java.util.Map;

public record VectorDocument(
        String id,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        String content,
        List<Float> embedding,
        Map<String, Object> metadata
) {}