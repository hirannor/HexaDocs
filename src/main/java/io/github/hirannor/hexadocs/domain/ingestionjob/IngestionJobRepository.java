package io.github.hirannor.hexadocs.domain.ingestionjob;

import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.util.List;
import java.util.Optional;

public interface IngestionJobRepository {

    void save(final IngestionJob job);

    Optional<IngestionJob> findById(final IngestionJobId id);

    Optional<IngestionJob> findByDocumentId(final DocumentId documentId);

    List<IngestionJob> findActiveJobs();

    boolean existsByDocumentId(final DocumentId documentId);
}