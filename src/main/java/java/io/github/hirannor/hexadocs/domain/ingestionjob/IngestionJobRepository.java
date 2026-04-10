package java.io.github.hirannor.hexadocs.domain.ingestionjob;

import java.util.Optional;
import java.util.List;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;

public interface IngestionJobRepository {

    void save(final IngestionJob job);

    Optional<IngestionJob> findById(final IngestionJobId id);

    Optional<IngestionJob> findByDocumentId(final DocumentId documentId);

    List<IngestionJob> findActiveJobs();

    boolean existsByDocumentId(final DocumentId documentId);
}