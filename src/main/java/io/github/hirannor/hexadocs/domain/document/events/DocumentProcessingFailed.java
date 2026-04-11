package io.github.hirannor.hexadocs.domain.document.events;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

import java.time.Instant;
import java.util.Objects;

public record DocumentProcessingFailed(
        MessageId id,
        IngestionJobId jobId,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        String error,
        Instant occurredAt
) implements DomainEvent {

    public static DocumentProcessingFailed record(
            final IngestionJobId jobId,
            final DocumentId documentId,
            final KnowledgeBaseId knowledgeBaseId,
            final String error
    ) {
        return new DocumentProcessingFailed(
                MessageId.generate(),
                Objects.requireNonNull(jobId),
                Objects.requireNonNull(documentId),
                Objects.requireNonNull(knowledgeBaseId),
                Objects.requireNonNull(error),
                Instant.now()
        );
    }
}