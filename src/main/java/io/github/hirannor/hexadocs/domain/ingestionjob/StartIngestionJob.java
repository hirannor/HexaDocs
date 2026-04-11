package io.github.hirannor.hexadocs.domain.ingestionjob;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

import java.time.Instant;

public record StartIngestionJob(
        CommandId id,
        Instant registeredAt,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId
) implements Command {

    public static StartIngestionJob issue(final DocumentId documentId, final KnowledgeBaseId knowledgeBaseId) {
        return new StartIngestionJob(CommandId.generate(), Instant.now(), documentId, knowledgeBaseId);
    }
}
