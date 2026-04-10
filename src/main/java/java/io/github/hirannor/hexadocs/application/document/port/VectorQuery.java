package java.io.github.hirannor.hexadocs.application.document.port;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public record VectorQuery(
        String text,
        KnowledgeBaseId knowledgeBaseId,
        int topK
) {}