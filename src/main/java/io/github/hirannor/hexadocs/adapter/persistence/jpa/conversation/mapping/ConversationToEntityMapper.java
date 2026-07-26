package io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation.mapping;


import io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation.ConversationEntity;
import io.github.hirannor.hexadocs.domain.conversation.Conversation;

import java.util.function.Function;

public class ConversationToEntityMapper implements Function<Conversation, ConversationEntity> {

    @Override
    public ConversationEntity apply(final Conversation domain) {
        if (domain == null) {
            return null;
        }

        final ConversationEntity entity = new ConversationEntity();
        entity.setConversationId(domain.id()
                .asText());
        entity.setKnowledgeBaseId(domain.knowledgeBaseId()
                .asText());
        entity.setTitle(domain.title());
        entity.setCreatedAt(domain.createdAt());

        return entity;
    }
}