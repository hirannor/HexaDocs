package java.io.github.hirannor.hexadocs.adapter.persistence.jpa;

import java.io.github.hirannor.hexadocs.domain.document.Document;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.domain.document.DocumentRepository;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.util.List;
import java.util.Optional;

class JpaDocumentRepository implements DocumentRepository {

    JpaDocumentRepository() {}

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
