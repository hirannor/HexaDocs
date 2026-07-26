package io.github.hirannor.hexadocs.domain.conversation;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;

import java.time.Instant;

public final class Conversation extends AggregateRoot {
    private final ConversationId id;
    private final KnowledgeBaseId knowledgeBaseId;
    private final Instant createdAt;
    private String title;

    public Conversation(final ConversationId id,
            final KnowledgeBaseId knowledgeBaseId,
            final String title,
            final Instant createdAt) {
        this.id = id;
        this.knowledgeBaseId = knowledgeBaseId;
        this.title = title;
        this.createdAt = createdAt;
    }

    public static Conversation create(final CreateConversation command) {
        Instant now = Instant.now();

        return new Conversation(command.conversationId(), command.knowledgeBaseId(), command.title(), now);
    }


    public ConversationId id() {
        return id;
    }

    public KnowledgeBaseId knowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String title() {
        return title;
    }

    public Instant createdAt() {
        return createdAt;
    }

}