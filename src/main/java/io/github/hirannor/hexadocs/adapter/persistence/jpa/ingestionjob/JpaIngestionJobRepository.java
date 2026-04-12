package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping.IngestionJobEntityToDomainMapper;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping.IngestionJobModeller;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
class JpaIngestionJobRepository implements IngestionJobRepository {

    private final Function<IngestionJobEntity, IngestionJob> mapToDomain;

    private final IngestionJobSpringDataJpaRepository ingestionJobs;

    JpaIngestionJobRepository(final IngestionJobSpringDataJpaRepository ingestionJobs) {
        this.ingestionJobs = ingestionJobs;
        this.mapToDomain = new IngestionJobEntityToDomainMapper();
    }

    @Override
    public void save(IngestionJob job) {
        IngestionJobEntity entity = ingestionJobs
                .findByIngestionJobId(job.id().asText())
                .orElseGet(IngestionJobEntity::new);

        IngestionJobModeller.applyChangesFrom(job).to(entity);

        ingestionJobs.save(entity);
    }

    @Override
    public Optional<IngestionJob> findById(final IngestionJobId id) {
        return ingestionJobs.findByIngestionJobId(id.asText())
                .map(mapToDomain);
    }

    @Override
    public Optional<IngestionJob> findByDocumentId(final DocumentId documentId) {
        return ingestionJobs.findByDocumentId(documentId.asText())
                .map(mapToDomain);
    }

    @Override
    public List<IngestionJob> findActiveJobs() {
        return ingestionJobs.findByStatusIn(
                        List.of(JobStatusEntity.PENDING, JobStatusEntity.RUNNING)
                )
                .stream()
                .map(mapToDomain)
                .toList();
    }

    @Override
    public boolean existsByDocumentId(final DocumentId documentId) {
        return ingestionJobs.existsByDocumentId(documentId.asText());
    }
}