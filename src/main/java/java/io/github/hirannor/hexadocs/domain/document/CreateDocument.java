package java.io.github.hirannor.hexadocs.domain.document;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;
import java.time.Instant;

public record CreateDocument(
        CommandId id,
        Instant registeredAt,
        KnowledgeBaseId knowledgeBaseId,
        String name
) implements Command {

    public static CreateDocument issue(final KnowledgeBaseId knowledgeBaseId, final String name) {
        return new CreateDocument(CommandId.generate(), Instant.now(), knowledgeBaseId, name);
    }
}