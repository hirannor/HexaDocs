package java.io.github.hirannor.hexadocs.domain.knowledgebase;


import java.io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;
import java.time.Instant;


public record CreateKnowledgeBase(
        CommandId id,
        Instant registeredAt,
        String name
) implements Command {

    public static CreateKnowledgeBase issue(final String name) {
        return new CreateKnowledgeBase(CommandId.generate(), Instant.now(), name);
    }
}