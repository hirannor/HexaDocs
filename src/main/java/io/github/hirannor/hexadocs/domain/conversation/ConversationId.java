package io.github.hirannor.hexadocs.domain.conversation;

import java.util.UUID;

public record ConversationId(UUID uniqueComponent) {

    public ConversationId {
        if (uniqueComponent == null) {
            throw new IllegalArgumentException("ConversationId can't be null");
        }
    }

    public static ConversationId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("ConversationId can't be null or empty");
        }

        return new ConversationId(UUID.fromString(source));
    }

    public static ConversationId generate() {
        return new ConversationId(UUID.randomUUID());
    }

    public String asText() {
        return uniqueComponent.toString();
    }
}