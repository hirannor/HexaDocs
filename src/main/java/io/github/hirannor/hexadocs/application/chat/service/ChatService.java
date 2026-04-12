package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.LlmClient;
import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.application.document.port.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.VectorSearchResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ChatService implements QuestionAsking {

    private static final int MAX_CONTEXT_CHARS = 12_000;

    private static final String SYSTEM_PROMPT = """
            You are a helpful assistant.
            Answer only using the provided context.
            If the context is insufficient, say you don't know.
            """;
    private static final String USER_PROMPT_TEMPLATE = """
            Context:
            %s
            
            Question:
            %s
            """;
    private final KnowledgeStore knowledgeStore;
    private final LlmClient llm;

    ChatService(final KnowledgeStore knowledgeStore,
                final LlmClient llm) {
        this.knowledgeStore = knowledgeStore;
        this.llm = llm;
    }

    @Override
    public Answer ask(final AskQuestion command) {
        final VectorQuery query = new VectorQuery(
                command.question(),
                command.knowledgeBaseId(),
                5
        );

        final List<VectorSearchResult> results = knowledgeStore.search(query);

        if (results.isEmpty()) {
            return Answer.of("I don't know based on the provided documents.");
        }

        final String context = buildContext(results);

        final String userPrompt = USER_PROMPT_TEMPLATE.formatted(
                context,
                command.question()
        );

        final String response = llm.generate(
                SYSTEM_PROMPT,
                userPrompt
        );

        return Answer.of(response);
    }

    private String buildContext(final List<VectorSearchResult> results) {
        final StringBuilder sb = new StringBuilder();

        for (VectorSearchResult r : results) {
            if (sb.length() > MAX_CONTEXT_CHARS) {
                break;
            }

            sb.append(r.content())
                    .append("\n---\n");
        }

        return sb.toString();
    }
}
