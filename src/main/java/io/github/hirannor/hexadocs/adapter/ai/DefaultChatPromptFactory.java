package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.ChatPromptFactory;
import io.github.hirannor.hexadocs.application.chat.port.PromptProvider;
import io.github.hirannor.hexadocs.application.chat.port.PromptType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DefaultChatPromptFactory implements ChatPromptFactory {

    private final PromptProvider promptProvider;

    DefaultChatPromptFactory(final PromptProvider promptProvider) {
        this.promptProvider = promptProvider;
    }

    @Override
    public String specificQuestion(
            final String question,
            final String context
    ) {
        return promptProvider
                .get(PromptType.SPECIFIC_QUESTION)
                .formatted(context, question);
    }

    @Override
    public String documentAnalysis(
            final String question,
            final String context
    ) {
        return promptProvider
                .get(PromptType.DOCUMENT_ANALYSIS)
                .formatted(context, question);
    }

    @Override
    public String documentWide(
            final String question,
            final List<String> analyses
    ) {
        return promptProvider
                .get(PromptType.DOCUMENT_WIDE)
                .formatted(
                        String.join("\n\n---\n\n", analyses),
                        question
                );
    }

    @Override
    public String system() {
        return promptProvider.get(PromptType.SYSTEM);
    }
}