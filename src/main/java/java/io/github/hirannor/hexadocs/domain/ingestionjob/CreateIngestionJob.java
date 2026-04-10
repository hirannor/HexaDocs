package java.io.github.hirannor.hexadocs.domain.ingestionjob;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;
import java.time.Instant;

public record CreateIngestionJob(
        CommandId id,
        Instant registeredAt,
        DocumentId documentId
) implements Command {

    public static CreateIngestionJob issue(final DocumentId documentId) {
        return new CreateIngestionJob(CommandId.generate(), Instant.now(), documentId);
    }
}
