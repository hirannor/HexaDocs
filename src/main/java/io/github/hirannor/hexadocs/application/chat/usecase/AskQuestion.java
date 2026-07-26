package io.github.hirannor.hexadocs.application.chat.usecase;

import io.github.hirannor.hexadocs.domain.conversation.ConversationId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

public record AskQuestion(CommandId id, ConversationId conversationId, String question) implements Command {

    public static AskQuestion issue(final ConversationId conversationId, final String question) {
        return new AskQuestion(CommandId.generate(), conversationId, question);
    }
}