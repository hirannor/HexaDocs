package io.github.hirannor.hexadocs.domain.ingestionjob;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

import java.time.Instant;

public record IngestionJobStarted(
        MessageId id,
        IngestionJobId jobId,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        Instant occurredAt
) implements DomainEvent {

    public static IngestionJobStarted record(
            final IngestionJobId jobId,
            final DocumentId documentId,
            final KnowledgeBaseId knowledgeBaseId
    ) {
        return new IngestionJobStarted(
                MessageId.generate(),
                jobId,
                documentId,
                knowledgeBaseId,
                Instant.now()
        );
    }
}