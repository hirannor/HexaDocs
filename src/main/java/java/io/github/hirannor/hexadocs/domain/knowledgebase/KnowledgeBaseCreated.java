package java.io.github.hirannor.hexadocs.domain.knowledgebase;

import java.io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import java.io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;
import java.time.Instant;

public record KnowledgeBaseCreated(
        MessageId id,
        KnowledgeBaseId knowledgeBaseId,
        String name,
        Instant occurredAt
) implements DomainEvent {

    public static KnowledgeBaseCreated record(
            final KnowledgeBaseId knowledgeBaseId,
            final String name
    ) {
        return new KnowledgeBaseCreated(
                MessageId.generate(),
                knowledgeBaseId,
                name,
                Instant.now()
        );
    }
}