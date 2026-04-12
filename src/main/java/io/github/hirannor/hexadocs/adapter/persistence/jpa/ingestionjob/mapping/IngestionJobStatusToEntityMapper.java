package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.JobStatusEntity;
import io.github.hirannor.hexadocs.domain.ingestionjob.JobStatus;

import java.util.function.Function;

class IngestionJobStatusToEntityMapper implements Function<JobStatus, JobStatusEntity> {
    @Override
    public JobStatusEntity apply(final JobStatus status) {
        if (status == null) return null;

        return switch (status) {
            case PENDING -> JobStatusEntity.PENDING;
            case RUNNING -> JobStatusEntity.RUNNING;
            case COMPLETED -> JobStatusEntity.COMPLETED;
            case FAILED -> JobStatusEntity.FAILED;
        };
    }
}