package io.github.hirannor.hexadocs.adapter.persistence.jpa;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class JpaIngestionJobRepository implements IngestionJobRepository {

    @Override
    public void save(final IngestionJob job) {

    }

    @Override
    public Optional<IngestionJob> findById(final IngestionJobId id) {
        return Optional.empty();
    }

    @Override
    public Optional<IngestionJob> findByDocumentId(final DocumentId documentId) {
        return Optional.empty();
    }

    @Override
    public List<IngestionJob> findActiveJobs() {
        return List.of();
    }

    @Override
    public boolean existsByDocumentId(final DocumentId documentId) {
        return false;
    }
}
