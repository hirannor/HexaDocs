package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface DocumentTextSpringDataJpaRepository extends Repository<DocumentTextEntity, Long> {
    Optional<DocumentTextEntity> findByDocumentId(final String documentId);

    void deleteByDocumentId(final String documentId);

    boolean existsByDocumentId(final String documentId);

    void save(final DocumentTextEntity entity);
}
