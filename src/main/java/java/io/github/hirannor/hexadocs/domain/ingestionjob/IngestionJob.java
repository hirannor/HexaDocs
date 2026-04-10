package java.io.github.hirannor.hexadocs.domain.ingestionjob;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IngestionJob extends AggregateRoot {
    private final IngestionJobId id;
    private final DocumentId documentId;
    private final List<String> errors;

    private JobStatus status;

    public IngestionJob(final IngestionJobId id, final DocumentId documentId) {
        this.id = Objects.requireNonNull(id);
        this.documentId = Objects.requireNonNull(documentId);
        this.status = JobStatus.PENDING;
        this.errors = new ArrayList<>();
    }

    public static IngestionJob create(final CreateIngestionJob command) {
        final IngestionJob job = new IngestionJob(
                IngestionJobId.generate(),
                command.documentId()
        );

        job.addEvent(IngestionJobCreated.record(job.id, job.documentId));

        return job;
    }

    public IngestionJobId id() {
        return id;
    }

    public DocumentId documentId() {
        return documentId;
    }

    public JobStatus status() {
        return status;
    }

    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }

}