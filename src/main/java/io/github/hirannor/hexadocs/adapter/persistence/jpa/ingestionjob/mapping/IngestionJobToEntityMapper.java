package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.IngestionJobEntity;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.JobStatusEntity;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.JobStatus;

import java.util.function.Function;

public class IngestionJobToEntityMapper implements Function<IngestionJob, IngestionJobEntity> {

    private final Function<JobStatus, JobStatusEntity> statusMapper;

    public IngestionJobToEntityMapper() {
        this.statusMapper = new IngestionJobStatusToEntityMapper();
    }

    @Override
    public IngestionJobEntity apply(final IngestionJob job) {

        final IngestionJobEntity entity = new IngestionJobEntity();

        entity.setIngestionJobId(job.id().asText());
        entity.setDocumentId(job.documentId().asText());
        entity.setKnowledgeBaseId(job.knowledgeBaseId().asText());

        entity.setStatus(statusMapper.apply(job.status()));

        return entity;
    }
}