package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;

public interface IngestionJobCompleting {
    void complete(final IngestionJobId jobId);
}