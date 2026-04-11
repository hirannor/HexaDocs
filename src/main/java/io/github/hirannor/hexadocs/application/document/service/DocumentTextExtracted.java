package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.DomainEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

public record DocumentTextExtracted(MessageId id, IngestionJobId ingestionJobId, DocumentId document, KnowledgeBaseId knowledgeBaseId) implements DomainEvent {
    public static DocumentTextExtracted record(final IngestionJobId ingestionJobId,
                                               final DocumentId document,
                                               final KnowledgeBaseId knowledgeBaseId) {
        return new DocumentTextExtracted(MessageId.generate(), ingestionJobId, document, knowledgeBaseId);
    }
}
