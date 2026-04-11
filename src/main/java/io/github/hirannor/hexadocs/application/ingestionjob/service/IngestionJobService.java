package io.github.hirannor.hexadocs.application.ingestionjob.service;

import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobRepository;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.springframework.stereotype.Service;

@Service
class IngestionJobService implements IngestionJobStarting {
    private final IngestionJobRepository ingestionJobs;
    private final MessagePublisher messages;


    IngestionJobService(final IngestionJobRepository ingestionJobs,
                        final MessagePublisher messages) {
        this.ingestionJobs = ingestionJobs;
        this.messages = messages;
    }

    @Override
    public IngestionJobId start(final StartIngestionJob command) {
        final IngestionJob job = IngestionJob.create(command);
        job.start();

        ingestionJobs.save(job);

        job.events()
                .forEach(messages::publish);

        return job.id();
    }
}
