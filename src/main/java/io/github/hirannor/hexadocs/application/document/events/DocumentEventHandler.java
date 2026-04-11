package io.github.hirannor.hexadocs.application.document.events;

import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobCompleting;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import io.github.hirannor.hexadocs.domain.document.events.DocumentProcessed;
import io.github.hirannor.hexadocs.domain.document.events.DocumentProcessingFailed;
import io.github.hirannor.hexadocs.domain.document.events.DocumentRegistered;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentEventHandler {

    private final IngestionJobStarting ingestionJob;
    private final IngestionJobCompleting ingestionJobCompleting;

    public DocumentEventHandler(final IngestionJobStarting ingestionJob,
                                final IngestionJobCompleting ingestionJobCompleting) {
        this.ingestionJob = ingestionJob;
        this.ingestionJobCompleting = ingestionJobCompleting;
    }

    @EventListener
    void handle(final DocumentRegistered event) {
        ingestionJob.start(StartIngestionJob.issue(event.documentId(), event.knowledgeBaseId()));
    }

    @EventListener
    void handle(final DocumentProcessed event) {
        ingestionJobCompleting.complete(event.jobId());
    }

    @EventListener
    void handle(final DocumentProcessingFailed event) {

    }
}
