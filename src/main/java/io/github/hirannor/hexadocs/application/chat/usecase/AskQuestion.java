package io.github.hirannor.hexadocs.application.chat.usecase;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

public record AskQuestion(
        CommandId id,
        KnowledgeBaseId knowledgeBaseId,
        String question
) implements Command {

    public static AskQuestion issue(final KnowledgeBaseId knowledgeBaseId, final String question) {
        return new AskQuestion(CommandId.generate(), knowledgeBaseId, question);
    }
}