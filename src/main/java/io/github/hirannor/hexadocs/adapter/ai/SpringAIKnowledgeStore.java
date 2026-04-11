package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.document.port.KnowledgeStore;
import io.github.hirannor.hexadocs.application.document.port.VectorDocument;
import io.github.hirannor.hexadocs.application.document.port.VectorQuery;
import io.github.hirannor.hexadocs.application.document.port.VectorSearchResult;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class SpringAIKnowledgeStore implements KnowledgeStore {

    private final VectorStore store;

    public SpringAIKnowledgeStore(final VectorStore store) {
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

        final List<Document> results = store.similaritySearch(
                SearchRequest.builder()
                        .query(query.text())
                        .topK(query.topK())
                        .filterExpression(filter)
                        .build()
        );

        if (results == null) {
            return List.of();
        }

        return results.stream()
                .map(this::toSearchResult)
                .toList();
    }


    private Document toSpringDocument(final VectorDocument doc) {
        final Map<String, Object> metadata = new HashMap<>(doc.metadata());

        metadata.put("documentId", doc.documentId().asText());
        metadata.put("knowledgeBaseId", doc.knowledgeBaseId().asText());

        return new Document(doc.id(), doc.content(), metadata);
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
        final Object score = doc.getMetadata().get("score");
        if (score instanceof Number n) {
            return n.doubleValue();
        }
        return 0.0;
    }
}