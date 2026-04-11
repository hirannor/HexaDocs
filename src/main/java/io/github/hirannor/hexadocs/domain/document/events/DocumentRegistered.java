package io.github.hirannor.hexadocs.domain.document.events;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

import java.time.Instant;

public record DocumentRegistered(
        MessageId id,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        String name,
        Instant occurredAt
) implements DomainEvent {

    public static DocumentRegistered record(final DocumentId documentId, final KnowledgeBaseId knowledgeBaseId, final String name) {
        return new DocumentRegistered(MessageId.generate(), documentId, knowledgeBaseId, name, Instant.now());
    }

}