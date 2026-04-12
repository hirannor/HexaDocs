package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentMetadataRepository;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class JpaDocumentMetadataRepository implements DocumentMetadataRepository {

    JpaDocumentMetadataRepository() {
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
