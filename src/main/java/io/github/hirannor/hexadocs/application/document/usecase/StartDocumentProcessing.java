package io.github.hirannor.hexadocs.application.document.usecase;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

import java.time.Instant;

public record StartDocumentProcessing(
        CommandId id,
        Instant registeredAt,
        IngestionJobId jobId,
        KnowledgeBaseId knowledgeBaseId,
        DocumentId documentId
) implements Command {

    public static StartDocumentProcessing issue(final IngestionJobId jobId, final KnowledgeBaseId knowledgeBaseId, final DocumentId documentId) {
        return new StartDocumentProcessing(CommandId.generate(), Instant.now(), jobId, knowledgeBaseId, documentId);
    }
}