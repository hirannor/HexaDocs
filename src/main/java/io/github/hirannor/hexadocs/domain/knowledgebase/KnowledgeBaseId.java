package io.github.hirannor.hexadocs.domain.knowledgebase;

import java.util.UUID;

public record KnowledgeBaseId(UUID uniqueComponent) {
    public KnowledgeBaseId {
        if (uniqueComponent == null)
            throw new IllegalArgumentException("KnowledgeBaseId can't be null");
    }

    public static KnowledgeBaseId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                    "KnowledgeBaseId can't be null or empty"
            );
        }

        return new KnowledgeBaseId(UUID.fromString(source));
    }

    public static KnowledgeBaseId generate() {
        return new KnowledgeBaseId(UUID.randomUUID());
    }

    public String asText() {
        return this.uniqueComponent.toString();
    }
}