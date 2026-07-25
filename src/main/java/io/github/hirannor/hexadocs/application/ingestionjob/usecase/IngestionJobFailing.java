package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

/**
 * Provides the use case for failing an ingestion job.
 */
public interface IngestionJobFailing {

    /**
     * Marks an ingestion job as failed.
     *
     * @param command the command containing the information about
     *                the failed ingestion job
     */
    void fail(final FailIngestionJob command);
}