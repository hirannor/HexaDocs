package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface IngestionJobSpringDataJpaRepository extends Repository<IngestionJobEntity, Long> {
    IngestionJobEntity save(final IngestionJobEntity entity);

    Optional<IngestionJobEntity> findByIngestionJobId(final String ingestionJobId);

    Optional<IngestionJobEntity> findByDocumentId(final String documentId);

    List<IngestionJobEntity> findByStatusIn(final List<JobStatusEntity> statuses);

    boolean existsByDocumentId(final String documentId);
}
