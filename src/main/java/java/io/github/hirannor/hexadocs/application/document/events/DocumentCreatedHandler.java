package java.io.github.hirannor.hexadocs.application.document.events;

import org.springframework.context.event.EventListener;

import java.io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import java.io.github.hirannor.hexadocs.domain.document.DocumentCreated;

public class DocumentCreatedHandler {

    private final IngestionJobStarting ingestionJob;

    public DocumentCreatedHandler(final IngestionJobStarting ingestionJob) {
        this.ingestionJob = ingestionJob;
    }

    @EventListener
    void handle(final DocumentCreated event) {
        ingestionJob.start(event.documentId());
    }
}
