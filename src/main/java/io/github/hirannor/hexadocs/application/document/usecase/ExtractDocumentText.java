package io.github.hirannor.hexadocs.application.document.usecase;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

public record ExtractDocumentText(CommandId id, IngestionJobId ingestionJobId, DocumentId documentId, KnowledgeBaseId knowledgeBaseId) implements Command {
    public static ExtractDocumentText issue(final IngestionJobId ingestionJobId, final DocumentId documentId, final KnowledgeBaseId knowledgeBaseId) {
        return new ExtractDocumentText(CommandId.generate(), ingestionJobId, documentId, knowledgeBaseId);
    }
}
