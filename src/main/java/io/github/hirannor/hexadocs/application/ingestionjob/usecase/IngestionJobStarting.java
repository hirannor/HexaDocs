package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;

public interface IngestionJobStarting {
    IngestionJobId start(final StartIngestionJob command);
}
