package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.document.port.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.VectorDocument;
import io.github.hirannor.hexadocs.application.document.port.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.VectorSearchResult;
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

    private final VectorStore store;

    public SpringAiKnowledgeStore(final VectorStore store) {
        this.store = store;
    }

    @Override
    public void store(final List<VectorDocument> documents) {
        final List<Document> aiDocuments = documents.stream()
                .map(this::toSpringDocument)
                .toList();

        store.add(aiDocuments);
    }

    @Override
    public List<VectorSearchResult> search(final VectorQuery query) {
        final String filter = buildFilter(query);

        final List<Document> documents = store.similaritySearch(
                SearchRequest.builder()
                        .query(query.text())
                        .topK(query.topK())
                        .similarityThreshold(0.5)
                        .filterExpression(filter)
                        .build()
        );

        if (documents == null) {
            return List.of();
        }

        final List<VectorSearchResult> results = documents.stream()
                .map(this::toSearchResult)
                .toList();

        results.forEach(r ->
                LOGGER.debug("Chunk | id={} | score={}",
                        r.id(),
                        r.score()
                )
        );

        return results;
    }

    private Document toSpringDocument(final VectorDocument doc) {
        final Map<String, Object> metadata = new HashMap<>(doc.metadata());

        return new Document(
                doc.id(),
                doc.content(),
                metadata
        );
    }
    private VectorSearchResult toSearchResult(final Document doc) {
        return new VectorSearchResult(
                doc.getId(),
                doc.getText(),
                extractScore(doc),
                doc.getMetadata()
        );
    }

    private String buildFilter(final VectorQuery query) {
        return "knowledgeBaseId == '" + query.knowledgeBaseId().asText() + "'";
    }

    private double extractScore(final Document doc) {
        return doc.getScore() != null ? doc.getScore() : 0.0;
    }
}