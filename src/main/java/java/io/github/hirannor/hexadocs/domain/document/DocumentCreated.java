package java.io.github.hirannor.hexadocs.domain.document;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import java.io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;
import java.time.Instant;

public record DocumentCreated(
        MessageId id,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        String name,
        Instant occurredAt
) implements DomainEvent {

    public static DocumentCreated record(final DocumentId documentId, final KnowledgeBaseId knowledgeBaseId, final String name) {
        return new DocumentCreated(MessageId.generate(), documentId, knowledgeBaseId, name, Instant.now());
    }

}