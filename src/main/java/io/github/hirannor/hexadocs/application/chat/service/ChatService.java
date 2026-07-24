package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.LlmClient;
import io.github.hirannor.hexadocs.application.chat.port.QueryClassifier;
import io.github.hirannor.hexadocs.application.chat.port.QueryType;
import io.github.hirannor.hexadocs.application.chat.port.SystemPromptProvider;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
class ChatService implements QuestionAsking {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

  private static final int MAX_CONTEXT_WINDOW_CHARS = 8_000;

  private static final String PAGE_NUMBER_METADATA_KEY = "pageNumber";
  private static final String CHUNK_ORDER_METADATA_KEY = "chunkOrder";
  private static final String UNKNOWN_ANSWER = "I don't know based on the provided documents.";

  private static final String USER_PROMPT_TEMPLATE = """
          <context>
          %s
          </context>

          <question>
          %s
          </question>
          """;

  private static final String DOCUMENT_ANALYSIS_PROMPT_TEMPLATE = """
          <task>
          Analyze this section of the document in relation to the question.

          Extract only facts explicitly supported by the provided context.
          Do not use information outside of the provided context.
          Do not invent missing information.

          If this section contains no relevant information,
          respond exactly with:
          NO_RELEVANT_INFORMATION

          Be concise.
          </task>

          <context>
          %s
          </context>

          <question>
          %s
          </question>
          """;

  private static final String DOCUMENT_WIDE_PROMPT_TEMPLATE = """
          <task>
          Answer the question using the document section analyses below.

          Combine relevant information.
          Use only information from the provided analyses.
          Do not invent information.

          If the analyses do not contain enough information,
          state that you do not know based on the provided documents.
          </task>

          <analyses>
          %s
          </analyses>

          <question>
          %s
          </question>
          """;

  private final KnowledgeStore knowledgeStore;
  private final DocumentContentReader documentContentReader;
  private final LlmClient llm;
  private final QueryClassifier query;
  private final MessagePublisher messages;
  private final SystemPromptProvider promptProvider;

  ChatService(final KnowledgeStore knowledgeStore, final DocumentContentReader documentContentReader,
              final LlmClient llm, final QueryClassifier query, final MessagePublisher messages,
              final SystemPromptProvider promptProvider) {
    this.knowledgeStore = knowledgeStore;
    this.documentContentReader = documentContentReader;
    this.llm = llm;
    this.query = query;
    this.messages = messages;
    this.promptProvider = promptProvider;
  }

  @Override
  public Answer ask(final AskQuestion command) {
    LOGGER.info("Received question | knowledgeBaseId={} | questionLength={}", command.knowledgeBaseId(),
            command.question().length());

    final QueryType queryType = query.classify(command.question());

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

    final String context = buildContext(results);

    if (context.isBlank()) {
      return createFallbackAnswer();
    }

    return generateAnswer(command.question(), context);
  }

  private Answer answerDocumentWideQuestion(final AskQuestion command) {
    final List<DocumentChunk> chunks = loadDocumentChunks(command);

    if (chunks.isEmpty()) {
      return createFallbackAnswer();
    }

    final List<String> windows = buildContextWindows(chunks);

    LOGGER.info("Document context prepared | chunks={} | windows={}", chunks.size(), windows.size());

    final List<String> analyses = windows.parallelStream()
            .map(window -> analyzeDocumentWindow(command.question(), window)).filter(this::isRelevantAnalysis).toList();

    LOGGER.info("Document analysis completed | relevantAnalyses={}", analyses.size());

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

  private String buildContext(final List<? extends ContextChunk> chunks) {
    final StringBuilder context = new StringBuilder();

    for (int i = 0; i < chunks.size(); i++) {
      final String source = formatSource(i, chunks.get(i));

      final int separatorLength = context.isEmpty() ? 0 : 5;

      if (context.length() + separatorLength + source.length() > MAX_CONTEXT_WINDOW_CHARS) {
        break;
      }

      if (!context.isEmpty()) {
        context.append("\n---\n");
      }

      context.append(source);
    }

    return context.toString();
  }

  private List<String> buildContextWindows(final List<? extends ContextChunk> chunks) {
    final List<String> windows = new ArrayList<>();
    final StringBuilder current = new StringBuilder();

    for (int i = 0; i < chunks.size(); i++) {
      final String source = formatSource(i, chunks.get(i));
      final int separatorLength = current.isEmpty() ? 0 : 5;

      if (!current.isEmpty() && current.length() + separatorLength + source.length() > MAX_CONTEXT_WINDOW_CHARS) {
        windows.add(current.toString());
        current.setLength(0);
      }

      if (!current.isEmpty()) {
        current.append("\n---\n");
      }
      current.append(source);
    }

    if (!current.isEmpty()) {
      windows.add(current.toString());
    }

    return windows;
  }

  private String analyzeDocumentWindow(final String question, final String context) {
    final String prompt = DOCUMENT_ANALYSIS_PROMPT_TEMPLATE.formatted(context, question);

    return llm.generate(promptProvider.get(), prompt);
  }

  private Answer synthesizeDocumentAnswer(final String question, final List<String> analyses) {
    final String prompt = DOCUMENT_WIDE_PROMPT_TEMPLATE.formatted(String.join("\n\n---\n\n", analyses), question);

    return Answer.of(llm.generate(promptProvider.get(), prompt));
  }

  private Answer generateAnswer(final String question, final String context) {
    final String prompt = USER_PROMPT_TEMPLATE.formatted(context, question);

    return Answer.of(llm.generate(promptProvider.get(), prompt));
  }

  private String formatSource(final int index, final ContextChunk chunk) {
    return """
            [Source %d]
            [Page: %s]
            [Chunk: %s]

            %s
            """.formatted(index + 1, extractMetadata(chunk, PAGE_NUMBER_METADATA_KEY),
            extractMetadata(chunk, CHUNK_ORDER_METADATA_KEY), chunk.content());
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

  private Object extractMetadata(final ContextChunk chunk, final String key) {
    return chunk.metadata().getOrDefault(key, "unknown");
  }

  private Answer createFallbackAnswer() {
    return Answer.of(UNKNOWN_ANSWER);
  }
}
