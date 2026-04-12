package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.IngestionJobEntity;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.JobStatusEntity;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.JobStatus;

import java.util.Optional;
import java.util.function.Function;

public class IngestionJobModeller {

    private final IngestionJob domain;
    private final Function<JobStatus, JobStatusEntity> mapToEntity;

    private IngestionJobModeller(final IngestionJob domain) {
        this.domain = domain;
        this.mapToEntity = new IngestionJobStatusToEntityMapper();
    }

    public static IngestionJobModeller applyChangesFrom(final IngestionJob domain) {
        return new IngestionJobModeller(domain);
    }

    public IngestionJobEntity to(final IngestionJobEntity entity) {
        if (entity == null) return null;

        entity.setIngestionJobId(domain.id().asText());

        Optional.ofNullable(domain.documentId())
                .ifPresent(d -> entity.setDocumentId(d.asText()));

        Optional.ofNullable(domain.knowledgeBaseId())
                .ifPresent(k -> entity.setKnowledgeBaseId(k.asText()));

        Optional.ofNullable(domain.status())
                .map(mapToEntity)
                .ifPresent(entity::setStatus);

        Optional.ofNullable(domain.error())
                .ifPresent(entity::setError);

        return entity;
    }
}