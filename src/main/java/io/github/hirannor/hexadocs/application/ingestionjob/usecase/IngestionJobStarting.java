package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;

/**
 * Provides the use case for starting an ingestion job.
 */
public interface IngestionJobStarting {

    /**
     * Starts an ingestion job.
     *
     * @param command the command containing the information required
     *                to start the ingestion job
     * @return the identifier of the started ingestion job
     */
    IngestionJobId start(final StartIngestionJob command);
}
