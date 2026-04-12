package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DocumentMetadataSpringDataJpaRepository extends Repository<DocumentEntity, Long> {
    Optional<DocumentEntity> findByDocumentId(String documentId);

    List<DocumentEntity> findByKnowledgeBaseId(String knowledgeBaseId);

    Optional<DocumentEntity> findByDocumentIdAndKnowledgeBaseId(
            String documentId,
            String knowledgeBaseId
    );

    boolean existsByDocumentId(String documentId);

    void deleteByDocumentId(String documentId);

    void save(DocumentEntity document);
}
