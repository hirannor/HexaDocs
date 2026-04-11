package io.github.hirannor.hexadocs.adapter.persistence.jpa;

import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentRepository;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class JpaDocumentRepository implements DocumentRepository {

    JpaDocumentRepository() {
    }

    @Override
    public void save(final Document document) {

    }

    @Override
    public Optional<Document> findById(final DocumentId id) {
        return Optional.empty();
    }

    @Override
    public List<Document> findByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId) {
        return List.of();
    }

    @Override
    public boolean existsById(final DocumentId id) {
        return false;
    }

    @Override
    public void deleteById(final DocumentId id) {

    }
}
