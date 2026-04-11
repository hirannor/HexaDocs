package io.github.hirannor.hexadocs.application.document.events;

import io.github.hirannor.hexadocs.application.document.usecase.DocumentProcessing;
import io.github.hirannor.hexadocs.application.document.usecase.StartDocumentProcessing;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import io.github.hirannor.hexadocs.domain.document.events.DocumentRegistered;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentEventHandler {

    private final IngestionJobStarting ingestionJob;
    private final DocumentProcessing document;

    public DocumentEventHandler(final IngestionJobStarting ingestionJob,
                                final DocumentProcessing document) {
        this.ingestionJob = ingestionJob;
        this.document = document;
    }

    @EventListener
    void handle(final DocumentRegistered event) {
        ingestionJob.start(StartIngestionJob.issue(event.documentId(), event.knowledgeBaseId()));
    }

    @EventListener
    void handle(final StartDocumentProcessing event) {
        document.process(StartDocumentProcessing.issue(
                event.jobId(),
                event.knowledgeBaseId(),
                event.documentId()
        ));
    }
}
