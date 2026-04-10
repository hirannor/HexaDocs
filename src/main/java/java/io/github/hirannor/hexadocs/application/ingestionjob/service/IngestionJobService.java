package java.io.github.hirannor.hexadocs.application.ingestionjob.service;

import org.springframework.stereotype.Service;

import java.io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import java.io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobRepository;

@Service
class IngestionJobService implements IngestionJobStarting {
    private final IngestionJobRepository ingestionJobs;

    IngestionJobService(final IngestionJobRepository ingestionJobs) {
        this.ingestionJobs = ingestionJobs;
    }

    @Override
    public IngestionJobId start(final DocumentId document) {
        return null;
    }
}
