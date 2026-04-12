package io.github.hirannor.hexadocs.application.ingestionjob.service;

import io.github.hirannor.hexadocs.application.ingestionjob.usecase.FailIngestionJob;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobCompleting;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobFailing;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobRepository;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class IngestionJobService implements IngestionJobStarting, IngestionJobCompleting, IngestionJobFailing {

    private static final Logger log = LoggerFactory.getLogger(IngestionJobService.class);

    private final IngestionJobRepository ingestionJobs;
    private final MessagePublisher messages;

    IngestionJobService(final IngestionJobRepository ingestionJobs,
                        final MessagePublisher messages) {
        this.ingestionJobs = ingestionJobs;
        this.messages = messages;
    }

    @Override
    public IngestionJobId start(final StartIngestionJob command) {
        log.info("Starting ingestion job | documentId={} | knowledgeBaseId={}",
                command.documentId().asText(),
                command.knowledgeBaseId().asText()
        );

        final IngestionJob job = IngestionJob.create(command);
        job.start();

        ingestionJobs.save(job);

        job.events()
                .forEach(messages::publish);

        log.info("Ingestion job started successfully | jobId={}", job.id().asText());

        return job.id();
    }

    @Override
    public void complete(final IngestionJobId jobId) {
        log.info("Completing ingestion job | jobId={}", jobId.asText());

        final IngestionJob job = ingestionJobs.findById(jobId)
                .orElseThrow(() -> new IllegalStateException("Job not found: " + jobId));

        job.complete();

        ingestionJobs.save(job);

        job.events().forEach(messages::publish);

        log.info("Ingestion job completed | jobId={}", jobId.asText());
    }

    @Override
    public void fail(final FailIngestionJob command) {
        log.info("Failing ingestion job | jobId={}", command.ingestionJobId().asText());

        final IngestionJob job = ingestionJobs.findById(command.ingestionJobId())
                .orElseThrow(() -> new IllegalStateException("Job not found: " + command.ingestionJobId()));

        job.fail(command.error());

        ingestionJobs.save(job);

        job.events().forEach(messages::publish);

        log.info("Ingestion job failed | jobId={}", command.ingestionJobId().asText());
    }
}