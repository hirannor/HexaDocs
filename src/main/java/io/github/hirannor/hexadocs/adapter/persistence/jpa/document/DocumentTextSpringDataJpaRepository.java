package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface DocumentTextSpringDataJpaRepository extends Repository<DocumentTextEntity, Long> {
    List<DocumentTextEntity> findAllByDocumentIdOrderByPageNumberAsc(String documentId);

    void deleteByDocumentId(String documentId);

    void saveAll(Iterable<DocumentTextEntity> entities);
}