package java.io.github.hirannor.hexadocs.domain.document;

import java.util.List;
import java.util.Optional;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public interface DocumentRepository {

    void save(final Document document);

    Optional<Document> findById(final DocumentId id);

    List<Document> findByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId);

    boolean existsById(final DocumentId id);

    void deleteById(final DocumentId id);
}