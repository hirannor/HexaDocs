package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.prompt.ChatPromptFactory;
import io.github.hirannor.hexadocs.application.chat.port.prompt.PromptProvider;
import io.github.hirannor.hexadocs.application.chat.port.prompt.PromptType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DefaultChatPromptFactory implements ChatPromptFactory {
    private final PromptProvider promptProvider;

    DefaultChatPromptFactory(final PromptProvider promptProvider) {
        this.promptProvider = promptProvider;
    }


    @Override
    public String specificQuestion(final String question, final String history, final String context) {
        return promptProvider.get(PromptType.QUERY_REWRITER_USER)
                .formatted(history, context, question);
    }

    @Override
    public String documentAnalysis(final String question, final String context) {
        return promptProvider.get(PromptType.DOCUMENT_ANALYSIS)
                .formatted(context, question);
    }

    @Override
    public String documentWide(final String question, final String history, final List<String> analyses) {
        return promptProvider.get(PromptType.DOCUMENT_WIDE)
                .formatted(history, String.join("\n\n---\n\n", analyses), question);
    }

    @Override
    public String system() {

        return promptProvider.get(PromptType.SYSTEM);
    }
}