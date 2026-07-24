package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorDocument;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.VectorSearchResult;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class SpringAiKnowledgeStore implements KnowledgeStore {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringAiKnowledgeStore.class);

  private static final String KNOWLEDGE_BASE_ID_METADATA_KEY = "knowledgeBaseId";

  private static final String DOCUMENT_PREFIX = "search_document: ";
  private static final String QUERY_PREFIX = "search_query: ";

  private final VectorStore store;

  private final SpringAiConfigurationProperties.RetrievalProperties retrievalProperties;

  SpringAiKnowledgeStore(final VectorStore store, final SpringAiConfigurationProperties properties) {
    this.store = store;
    this.retrievalProperties = properties.chat().retrieval();
  }

  @Override
  public void store(final List<VectorDocument> documents) {
    if (documents.isEmpty()) {
      return;
    }

    final List<Document> aiDocuments = documents.stream().map(this::toSpringDocument).toList();

    LOGGER.debug("Storing vector documents | count={}", aiDocuments.size());

    aiDocuments.forEach(document -> LOGGER.trace("Vector document | id={} | metadata={} | content={}", document.getId(),
            document.getMetadata(), document.getText()));

    store.add(aiDocuments);
  }

  @Override
  public List<VectorSearchResult> search(final VectorQuery query) {
    final String prefixedQueryText = QUERY_PREFIX + query.text();

    final SearchRequest request = SearchRequest.builder().query(prefixedQueryText).topK(retrievalProperties.topK())
            .similarityThreshold(retrievalProperties.similarityThreshold())
            .filterExpression(buildKnowledgeBaseFilter(query)).build();

    final List<Document> documents = store.similaritySearch(request);

    if (documents == null || documents.isEmpty()) {
      LOGGER.debug("No vector search results | knowledgeBaseId={} | query={}", query.knowledgeBaseId().asText(),
              query.text());

      return List.of();
    }

    final List<VectorSearchResult> results = documents.stream().map(this::toSearchResult).toList();

    LOGGER.debug("Vector search completed | knowledgeBaseId={} | resultCount={}", query.knowledgeBaseId().asText(),
            results.size());

    results.forEach(result -> LOGGER.debug("Chunk | id={} | score={} | content={}", result.id(), result.score(),
            result.content()));

    return results;
  }

  private Document toSpringDocument(final VectorDocument vectorDocument) {
    final Map<String, Object> metadata = new HashMap<>(vectorDocument.metadata());

    metadata.put(KNOWLEDGE_BASE_ID_METADATA_KEY, vectorDocument.knowledgeBaseId().asText());

    final String prefixedContent = DOCUMENT_PREFIX + vectorDocument.content();

    return new Document(vectorDocument.id(), prefixedContent, metadata);
  }

  private VectorSearchResult toSearchResult(final Document document) {
    return new VectorSearchResult(document.getId(), document.getText(), extractScore(document), document.getMetadata());
  }

  private String buildKnowledgeBaseFilter(final VectorQuery query) {
    return buildKnowledgeBaseFilter(query.knowledgeBaseId());
  }

  private String buildKnowledgeBaseFilter(final KnowledgeBaseId knowledgeBaseId) {
    return "%s == '%s'".formatted(KNOWLEDGE_BASE_ID_METADATA_KEY, knowledgeBaseId.asText());
  }

  private double extractScore(final Document document) {
    return document.getScore() == null ? 0.0 : document.getScore();
  }
}
