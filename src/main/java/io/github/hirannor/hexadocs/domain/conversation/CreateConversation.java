package io.github.hirannor.hexadocs.domain.conversation;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

import java.time.Instant;

public record CreateConversation(CommandId id, Instant registeredAt, ConversationId conversationId,
                                 KnowledgeBaseId knowledgeBaseId, String title) implements Command {

    public static CreateConversation issue(final KnowledgeBaseId knowledgeBaseId, final String title) {
        return new CreateConversation(CommandId.generate(), Instant.now(), ConversationId.generate(), knowledgeBaseId,
                title);
    }
}