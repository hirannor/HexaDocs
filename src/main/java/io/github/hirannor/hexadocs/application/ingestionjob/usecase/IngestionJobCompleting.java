package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;

/**
 * Provides the use case for completing an ingestion job.
 */
public interface IngestionJobCompleting {

    /**
     * Marks an ingestion job as completed.
     *
     * @param jobId the identifier of the ingestion job to complete
     */
    void complete(final IngestionJobId jobId);
}