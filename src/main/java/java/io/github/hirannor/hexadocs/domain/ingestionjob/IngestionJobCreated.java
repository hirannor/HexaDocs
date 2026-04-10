package java.io.github.hirannor.hexadocs.domain.ingestionjob;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import java.io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;
import java.time.Instant;

public record IngestionJobCreated(
        MessageId id,
        IngestionJobId jobId,
        DocumentId documentId,
        Instant occurredAt
) implements DomainEvent {

    public static IngestionJobCreated record(
            final IngestionJobId jobId,
            final DocumentId documentId
    ) {
        return new IngestionJobCreated(
                MessageId.generate(),
                jobId,
                documentId,
                Instant.now()
        );
    }
}