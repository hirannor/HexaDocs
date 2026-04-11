package io.github.hirannor.hexadocs.domain.ingestionjob;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IngestionJob extends AggregateRoot {

    private final IngestionJobId id;
    private final DocumentId documentId;
    private final KnowledgeBaseId knowledgeBaseId;

    private final List<String> errors = new ArrayList<>();

    private JobStatus status;

    private IngestionJob(IngestionJobId id,
                         DocumentId documentId,
                         KnowledgeBaseId knowledgeBaseId) {
        this.id = Objects.requireNonNull(id);
        this.documentId = Objects.requireNonNull(documentId);
        this.knowledgeBaseId = Objects.requireNonNull(knowledgeBaseId);
        this.status = JobStatus.PENDING;
    }

    public static IngestionJob create(StartIngestionJob command) {
        return new IngestionJob(
                IngestionJobId.generate(),
                command.documentId(),
                command.knowledgeBaseId()
        );
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

        status = JobStatus.FAILED;
        errors.add(error);
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

    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }
}