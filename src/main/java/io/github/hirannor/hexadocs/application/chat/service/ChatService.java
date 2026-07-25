package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.*;
import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.application.document.port.storage.content.DocumentChunk;
import io.github.hirannor.hexadocs.application.document.port.storage.content.DocumentContentReader;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.ContextChunk;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorSearchResult;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
class ChatService implements QuestionAsking {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private static final String CHUNK_ORDER_METADATA_KEY = "chunkOrder";
    private static final String UNKNOWN_ANSWER = "I don't know based on the provided documents.";


    private final KnowledgeStore knowledgeStore;
    private final DocumentContentReader documentContentReader;
    private final LlmClient llm;
    private final QueryClassifier queryClassifier;
    private final MessagePublisher messages;

    private final ChatPromptFactory promptFactory;
    private final ContextBuilder context;

    ChatService(final KnowledgeStore knowledgeStore, final DocumentContentReader documentContentReader,
                final LlmClient llm, final QueryClassifier queryClassifier, final MessagePublisher messages,
                final ContextBuilder context, ChatPromptFactory promptFactory) {
        this.knowledgeStore = knowledgeStore;
        this.documentContentReader = documentContentReader;
        this.llm = llm;
        this.queryClassifier = queryClassifier;
        this.messages = messages;
        this.context = context;
        this.promptFactory = promptFactory;
    }

    @Override
    public Answer ask(final AskQuestion command) {
        LOGGER.info("Received question | knowledgeBaseId={} | questionLength={}", command.knowledgeBaseId(),
                command.question().length());

        final QueryType queryType = queryClassifier.classify(command.question());

        LOGGER.info("Question classified | queryType={} | knowledgeBaseId={}", queryType, command.knowledgeBaseId());

        final Answer answer = switch (queryType) {
            case SPECIFIC -> answerSpecificQuestion(command);
            case DOCUMENT_WIDE -> answerDocumentWideQuestion(command);
        };

        messages.publish(AnswerGenerated.record(answer));

        LOGGER.info("Question answered | knowledgeBaseId={} | answerLength={}", command.knowledgeBaseId(),
                answer.content().length());

        return answer;
    }

    private Answer answerSpecificQuestion(final AskQuestion command) {
        final List<VectorSearchResult> results = searchRelevantChunks(command);

        if (results.isEmpty()) {
            return createFallbackAnswer();
        }

        final String builtContext = context.build(results);

        if (builtContext.isBlank()) {
            return createFallbackAnswer();
        }

        return generateAnswer(command.question(), builtContext);
    }

    private Answer answerDocumentWideQuestion(final AskQuestion command) {
        final List<DocumentChunk> chunks = loadDocumentChunks(command);

        if (chunks.isEmpty()) {
            return createFallbackAnswer();
        }

        final List<String> windows = context.buildWindows(chunks);

        final List<String> analyses = windows.parallelStream()
                .map(window -> analyzeDocumentWindow(command.question(), window)).filter(this::isRelevantAnalysis)
                .toList();

        if (analyses.isEmpty()) {
            return createFallbackAnswer();
        }

        return synthesizeDocumentAnswer(command.question(), analyses);
    }

    private List<VectorSearchResult> searchRelevantChunks(final AskQuestion command) {
        return knowledgeStore.search(new VectorQuery(command.question(), command.knowledgeBaseId()));
    }

    private List<DocumentChunk> loadDocumentChunks(final AskQuestion command) {
        return documentContentReader.readAll(command.knowledgeBaseId()).stream()
                .sorted(Comparator.comparingInt(this::extractChunkOrder)).toList();
    }

    private String analyzeDocumentWindow(final String question, final String context) {
        return llm.generate(promptFactory.system(), promptFactory.documentAnalysis(question, context));
    }

    private Answer synthesizeDocumentAnswer(final String question, final List<String> analyses) {
        return Answer.of(llm.generate(promptFactory.system(), promptFactory.documentWide(question, analyses)));
    }

    private Answer generateAnswer(final String question, final String context) {
        return Answer.of(llm.generate(promptFactory.system(), promptFactory.specificQuestion(question, context)));
    }

    private boolean isRelevantAnalysis(final String analysis) {
        if (analysis == null || analysis.isBlank()) {
            return false;
        }
        final String sanitized = analysis.trim().toUpperCase();
        return !sanitized.contains("NO_RELEVANT_INFORMATION");
    }

    private int extractChunkOrder(final ContextChunk chunk) {
        final Object value = chunk.metadata().get(CHUNK_ORDER_METADATA_KEY);

        return value instanceof Number number ? number.intValue() : Integer.MAX_VALUE;
    }

    private Answer createFallbackAnswer() {
        return Answer.of(UNKNOWN_ANSWER);
    }
}
