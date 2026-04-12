package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.JobStatusEntity;
import io.github.hirannor.hexadocs.domain.ingestionjob.JobStatus;

import java.util.function.Function;

class IngestionJobStatusEntityToDomainMapper implements Function<JobStatusEntity, JobStatus> {
    @Override
    public JobStatus apply(final JobStatusEntity status) {
        if (status == null) return null;

        return switch (status) {
            case PENDING -> JobStatus.PENDING;
            case RUNNING -> JobStatus.RUNNING;
            case COMPLETED -> JobStatus.COMPLETED;
            case FAILED -> JobStatus.FAILED;
        };
    }
}