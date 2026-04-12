package io.github.hirannor.hexadocs.domain.ingestionjob;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;

import java.util.Objects;

public class IngestionJob extends AggregateRoot {

    private final IngestionJobId id;
    private final DocumentId documentId;
    private final KnowledgeBaseId knowledgeBaseId;

    private String error;
    private JobStatus status;

    IngestionJob(final IngestionJobId id,
                 final DocumentId documentId,
                 final KnowledgeBaseId knowledgeBaseId,
                 final JobStatus status,
                 final String error) {
        this.id = Objects.requireNonNull(id);
        this.documentId = Objects.requireNonNull(documentId);
        this.knowledgeBaseId = Objects.requireNonNull(knowledgeBaseId);
        this.status = Objects.requireNonNull(status);
        this.error = error;
    }

    public static IngestionJobBuilder empty() {
        return new IngestionJobBuilder();
    }

    public static IngestionJob create(StartIngestionJob command) {
        return new IngestionJobBuilder()
                .id(IngestionJobId.generate())
                .documentId(command.documentId())
                .kbId(command.knowledgeBaseId())
                .status(JobStatus.PENDING)
                .create();
    }

    public void start() {
        if (status != JobStatus.PENDING) {
            throw new IllegalStateException("Job already started or finished: " + id);
        }

        status = JobStatus.RUNNING;

        addEvent(IngestionJobStarted.record(
                id,
                documentId,
                knowledgeBaseId
        ));
    }

    public void complete() {
        if (status != JobStatus.RUNNING) {
            throw new IllegalStateException("Job not running: " + id);
        }

        status = JobStatus.COMPLETED;
    }

    public void fail(final String error) {
        if (status != JobStatus.RUNNING) {
            throw new IllegalStateException("Job not running: " + id);
        }

        this.status = JobStatus.FAILED;
        this.error = error;
    }

    public IngestionJobId id() {
        return id;
    }

    public DocumentId documentId() {
        return documentId;
    }

    public KnowledgeBaseId knowledgeBaseId() {
        return knowledgeBaseId;
    }

    public JobStatus status() {
        return status;
    }

    public String error() {
        return error;
    }
}