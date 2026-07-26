package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import io.github.hirannor.hexadocs.application.chat.port.prompt.PromptProvider;
import io.github.hirannor.hexadocs.application.chat.port.prompt.PromptType;
import io.github.hirannor.hexadocs.application.chat.port.query.rewriter.QueryRewriter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class SpringAiQueryRewriter implements QueryRewriter {


    private final ChatClient chatClient;
    private final PromptProvider promptProvider;

    SpringAiQueryRewriter(final ChatClient chatClient, final PromptProvider promptProvider) {
        this.chatClient = chatClient;
        this.promptProvider = promptProvider;
    }

    @Override
    public String rewrite(final String question, final List<ConversationMessage> history) {
        if (history.isEmpty()) {
            return question;
        }

        final String conversation = formatHistory(history);
        final String systemPrompt = promptProvider.get(PromptType.QUERY_REWRITER_SYSTEM);
        final String userPrompt = promptProvider.get(PromptType.QUERY_REWRITER_USER)
                .formatted(conversation, question);

        final String rewrittenQuery = chatClient.prompt()
                .options(ChatOptions.builder()
                        .temperature(0.0)
                        .maxTokens(50)
                        .build())
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

        return sanitize(rewrittenQuery, question);
    }

    private String formatHistory(final List<ConversationMessage> history) {
        final StringBuilder builder = new StringBuilder();

        for (final ConversationMessage message : history) {
            builder.append(message.role())
                    .append(": ")
                    .append(message.content())
                    .append('\n');
        }

        return builder.toString();
    }

    private String sanitize(final String rewrittenQuery, final String originalQuestion) {
        if (rewrittenQuery == null || rewrittenQuery.isBlank()) {

            return originalQuestion;
        }

        String query = rewrittenQuery.trim()
                .replaceAll("\\s+", " ");

        final int newline = query.indexOf('\n');

        if (newline >= 0) {
            query = query.substring(0, newline);
        }

        if (query.length() > 300) {
            return originalQuestion;
        }

        return query;
    }
}
