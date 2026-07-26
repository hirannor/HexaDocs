package io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation.mapping;


import io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation.ConversationEntity;
import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.conversation.ConversationId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.function.Function;

public class ConversationEntityToDomainMapper implements Function<ConversationEntity, Conversation> {

    @Override
    public Conversation apply(final ConversationEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Conversation(ConversationId.from(entity.getConversationId()),
                KnowledgeBaseId.from(entity.getKnowledgeBaseId()), entity.getTitle(), entity.getCreatedAt());
    }
}