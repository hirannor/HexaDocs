package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.LlmClient;
import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.application.document.port.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.VectorSearchResult;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ChatService implements QuestionAsking {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private static final int MAX_CONTEXT_CHARS = 4000;

    private static final String SYSTEM_PROMPT = """
            You are a retrieval-augmented assistant working ONLY with provided context chunks from a document knowledge base.
            
            Rules:
            1. Use ONLY the information present in the provided context.
            2. Do NOT use external knowledge or prior training data.
            3. If the answer is not explicitly found in the context, say: "I don't know based on the provided documents."
            4. If the context is partially relevant, combine only the relevant parts without guessing or adding assumptions.
            5. Treat each context chunk as a source snippet from a larger document collection.
            6. If multiple chunks contain relevant information, synthesize them carefully without inventing details.
            7. Do not hallucinate missing steps, definitions, or explanations.
            
            Behavior:
            - Be precise and factual.
            - Prefer quoting or closely paraphrasing the context.
            - Keep answers concise and directly related to the question.
            """;

    private static final String USER_PROMPT_TEMPLATE = """
            Context:
            %s

            Question:
            %s
            """;

    private final KnowledgeStore knowledgeStore;
    private final LlmClient llm;
    private final MessagePublisher messages;

    ChatService(final KnowledgeStore knowledgeStore,
                final LlmClient llm,
                final MessagePublisher messages) {
        this.knowledgeStore = knowledgeStore;
        this.llm = llm;
        this.messages = messages;
    }

    @Override
    public Answer ask(final AskQuestion command) {

        LOGGER.info("Received question | knowledgeBaseId={} | questionLength={}",
                command.knowledgeBaseId(),
                command.question() != null ? command.question().length() : 0
        );

        final VectorQuery query = new VectorQuery(
                command.question(),
                command.knowledgeBaseId(),
                3
        );

        LOGGER.info("Executing vector search | topK=5");

        final List<VectorSearchResult> results = knowledgeStore.search(query);

        LOGGER.info("Vector search completed | resultsCount={}", results.size());

        if (results.isEmpty()) {
            LOGGER.info("No context found for question, returning fallback answer");

            final Answer fallbackAnswer = Answer.of("I don't know based on the provided documents.");
            messages.publish(AnswerGenerated.record(fallbackAnswer));
            return fallbackAnswer;
        }

        final String context = buildContext(results);

        LOGGER.info("Context built | contextLength={} | maxAllowed={}",
                context.length(),
                MAX_CONTEXT_CHARS
        );

        final String userPrompt = USER_PROMPT_TEMPLATE.formatted(
                context,
                command.question()
        );

        LOGGER.info("Calling LLM for response generation");

        final String response = llm.generate(
                SYSTEM_PROMPT,
                userPrompt
        );

        LOGGER.info("LLM response received | responseLength={}", response.length());

        final Answer answer = Answer.of(response);
        messages.publish(AnswerGenerated.record(answer));

        return Answer.of(response);
    }

    private String buildContext(final List<VectorSearchResult> results) {
        final StringBuilder sb = new StringBuilder();

        for (final VectorSearchResult r : results) {
            if (sb.length() > MAX_CONTEXT_CHARS) {
                break;
            }

            sb.append(r.content())
                    .append("\n---\n");
        }

        return sb.toString();
    }
}