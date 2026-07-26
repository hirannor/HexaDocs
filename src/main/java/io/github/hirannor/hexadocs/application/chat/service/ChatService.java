package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.LlmClient;
import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMemory;
import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import io.github.hirannor.hexadocs.application.chat.port.prompt.ChatPromptFactory;
import io.github.hirannor.hexadocs.application.chat.port.query.classifier.QueryClassifier;
import io.github.hirannor.hexadocs.application.chat.port.query.classifier.QueryType;
import io.github.hirannor.hexadocs.application.chat.port.query.rewriter.QueryRewriter;
import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.application.chat.usecase.AskQuestion;
import io.github.hirannor.hexadocs.application.chat.usecase.QuestionAsking;
import io.github.hirannor.hexadocs.application.document.port.storage.content.DocumentChunk;
import io.github.hirannor.hexadocs.application.document.port.storage.content.DocumentContentReader;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.ContextChunk;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorSearchResult;
import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.conversation.ConversationNotFound;
import io.github.hirannor.hexadocs.domain.conversation.ConversationRepository;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
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

    private static final int MAX_QUERY_REWRITER_HISTORY_MESSAGES = 6;

    private final ConversationRepository conversationRepository;
    private final KnowledgeStore knowledgeStore;
    private final DocumentContentReader documentContentReader;
    private final LlmClient llm;
    private final QueryClassifier queryClassifier;
    private final QueryRewriter queryRewriter;
    private final MessagePublisher messages;
    private final ChatPromptFactory promptFactory;
    private final ContextBuilder context;
    private final ConversationMemory conversationMemory;
    private final ConversationHistoryFormatter historyFormatter;

    ChatService(final ConversationRepository conversationRepository,
            final KnowledgeStore knowledgeStore,
            final DocumentContentReader documentContentReader,
            final LlmClient llm,
            final QueryClassifier queryClassifier,
            final QueryRewriter queryRewriter,
            final MessagePublisher messages,
            final ChatPromptFactory promptFactory,
            final ContextBuilder context,
            final ConversationMemory conversationMemory,
            final ConversationHistoryFormatter historyFormatter) {
        this.conversationRepository = conversationRepository;
        this.knowledgeStore = knowledgeStore;
        this.documentContentReader = documentContentReader;
        this.llm = llm;
        this.queryClassifier = queryClassifier;
        this.queryRewriter = queryRewriter;
        this.messages = messages;
        this.promptFactory = promptFactory;
        this.context = context;
        this.conversationMemory = conversationMemory;
        this.historyFormatter = historyFormatter;
    }

    @Override
    public Answer ask(final AskQuestion command) {
        final Conversation conversation = conversationRepository.findById(command.conversationId())
                .orElseThrow(() -> new ConversationNotFound(
                        "Conversation not found with id: " + command.conversationId()
                                .asText()));
        final KnowledgeBaseId knowledgeBaseId = conversation.knowledgeBaseId();

        final String conversationId = command.conversationId()
                .asText();

        final String question = command.question();

        LOGGER.info("Received question | conversationId={} | knowledgeBaseId={} | questionLength={}", conversationId,
                knowledgeBaseId.asText(), question.length());

        final List<ConversationMessage> conversationHistory = conversationMemory.get(conversationId);

        final String history = historyFormatter.format(conversationHistory);

        final QueryType queryType = queryClassifier.classify(question);

        LOGGER.info("Question classified | queryType={} | conversationId={} | knowledgeBaseId={}", queryType,
                conversationId, knowledgeBaseId.asText());

        final Answer answer = switch (queryType) {
            case SPECIFIC -> answerSpecificQuestion(command, knowledgeBaseId, conversationHistory, history);

            case DOCUMENT_WIDE -> answerDocumentWideQuestion(command, knowledgeBaseId, history);
        };

        conversationMemory.add(conversationId,
                List.of(ConversationMessage.user(question), ConversationMessage.assistant(answer.content())));

        messages.publish(AnswerGenerated.record(answer));

        LOGGER.info("Question answered | conversationId={} | knowledgeBaseId={} | answerLength={}", conversationId,
                knowledgeBaseId.asText(), answer.content()
                        .length());

        return answer;
    }

    private Answer answerSpecificQuestion(final AskQuestion command,
            final KnowledgeBaseId knowledgeBaseId,
            final List<ConversationMessage> conversationHistory,
            final String history) {
        final List<ConversationMessage> rewriterHistory = limitHistory(conversationHistory);

        final String rewrittenQuery = queryRewriter.rewrite(command.question(), rewriterHistory);

        LOGGER.debug("Query rewritten | original={} | rewritten={}", command.question(), rewrittenQuery);

        final List<VectorSearchResult> results = knowledgeStore.search(
                new VectorQuery(rewrittenQuery, knowledgeBaseId));

        LOGGER.debug("Specific question retrieval | rewrittenQuery={} | resultCount={}", rewrittenQuery,
                results.size());

        if (results.isEmpty()) {
            return createFallbackAnswer();
        }

        final String builtContext = context.build(results);

        if (builtContext.isBlank()) {
            return createFallbackAnswer();
        }

        return Answer.of(llm.generate(promptFactory.system(),
                promptFactory.specificQuestion(command.question(), history, builtContext)));
    }

    private Answer answerDocumentWideQuestion(final AskQuestion command,
            final KnowledgeBaseId knowledgeBaseId,
            final String history) {
        final List<DocumentChunk> chunks = documentContentReader.readAll(knowledgeBaseId)
                .stream()
                .sorted(Comparator.comparingInt(this::extractChunkOrder))
                .toList();

        LOGGER.debug("Document-wide processing | chunkCount={}", chunks.size());

        if (chunks.isEmpty()) {
            return createFallbackAnswer();
        }

        final List<String> windows = context.buildWindows(chunks);

        LOGGER.debug("Document-wide processing | windowCount={}", windows.size());

        final List<String> analyses = windows.parallelStream()
                .map(window -> analyzeDocumentWindow(command.question(), window))
                .filter(this::isRelevantAnalysis)
                .toList();

        LOGGER.debug("Document-wide processing | analysisCount={}", analyses.size());

        if (analyses.isEmpty()) {
            return createFallbackAnswer();
        }

        return Answer.of(llm.generate(promptFactory.system(),
                promptFactory.documentWide(command.question(), history, analyses)));
    }

    private String analyzeDocumentWindow(final String question, final String context) {
        return llm.generate(promptFactory.system(), promptFactory.documentAnalysis(question, context));
    }

    private List<ConversationMessage> limitHistory(final List<ConversationMessage> history) {
        if (history.size() <= MAX_QUERY_REWRITER_HISTORY_MESSAGES) {
            return history;
        }

        return history.subList(history.size() - MAX_QUERY_REWRITER_HISTORY_MESSAGES, history.size());
    }

    private boolean isRelevantAnalysis(final String analysis) {
        if (analysis == null || analysis.isBlank()) {
            return false;
        }

        return !analysis.trim()
                .toUpperCase()
                .contains("NO_RELEVANT_INFORMATION");
    }

    private int extractChunkOrder(final ContextChunk chunk) {
        final Object value = chunk.metadata()
                .get(CHUNK_ORDER_METADATA_KEY);

        return value instanceof Number number ? number.intValue() : Integer.MAX_VALUE;
    }

    private Answer createFallbackAnswer() {
        return Answer.of(UNKNOWN_ANSWER);
    }
}