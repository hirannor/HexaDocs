package io.github.hirannor.hexadocs.domain.document;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;
import java.util.Optional;

public interface DocumentMetadataRepository {

    void save(final Document document);

    Optional<Document> findById(final DocumentId id);

    List<Document> findByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId);

    boolean existsById(final DocumentId id);

    void deleteById(final DocumentId id);
}