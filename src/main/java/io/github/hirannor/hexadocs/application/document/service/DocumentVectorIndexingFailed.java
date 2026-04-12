package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.ApplicationEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

public record DocumentVectorIndexingFailed(MessageId id, IngestionJobId ingestionJobId, String error) implements ApplicationEvent {
    public static DocumentVectorIndexingFailed record(final IngestionJobId ingestionJobId,
                                                      final String error) {
        return new DocumentVectorIndexingFailed(MessageId.generate(), ingestionJobId, error);
    }
}
