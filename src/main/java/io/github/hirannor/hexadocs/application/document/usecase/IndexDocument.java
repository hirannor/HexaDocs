package io.github.hirannor.hexadocs.application.document.usecase;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

public record IndexDocument(CommandId id, IngestionJobId ingestionJobId, KnowledgeBaseId knowledgeBaseId, DocumentId documentId) implements Command {
    public static IndexDocument issue(final IngestionJobId ingestionJobId, final KnowledgeBaseId knowledgeBaseId, final DocumentId documentId) {
        return new IndexDocument(CommandId.generate(), ingestionJobId, knowledgeBaseId, documentId);
    }
}
