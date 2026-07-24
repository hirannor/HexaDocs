package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public record VectorQuery(String text, KnowledgeBaseId knowledgeBaseId) {
}
