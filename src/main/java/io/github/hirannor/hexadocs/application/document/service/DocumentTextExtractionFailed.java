package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.ApplicationEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

public record DocumentTextExtractionFailed(MessageId id, IngestionJobId ingestionJobId, String error) implements ApplicationEvent {
    public static DocumentTextExtractionFailed record(final IngestionJobId ingestionJobId,
                                                      final String error) {
        return new DocumentTextExtractionFailed(MessageId.generate(), ingestionJobId, error);
    }
}
