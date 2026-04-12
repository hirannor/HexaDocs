package io.github.hirannor.hexadocs.application.document.usecase;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.ApplicationEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;


public record DocumentVectorIndexed(MessageId id, IngestionJobId ingestionJobId, DocumentId document,
                                    KnowledgeBaseId knowledgeBaseId, int vectorSize) implements ApplicationEvent {
    public static DocumentVectorIndexed record(final IngestionJobId ingestionJobId,
                                               final DocumentId document,
                                               final KnowledgeBaseId knowledgeBaseId,
                                               final int vectorSize) {
        return new DocumentVectorIndexed(MessageId.generate(), ingestionJobId, document, knowledgeBaseId, vectorSize);
    }
}
