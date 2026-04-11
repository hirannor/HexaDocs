package io.github.hirannor.hexadocs.application.ingestionjob.events;

import io.github.hirannor.hexadocs.application.document.usecase.DocumentProcessing;
import io.github.hirannor.hexadocs.application.document.usecase.StartDocumentProcessing;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobStarted;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class IngestionJobEventHandler {
    private final DocumentProcessing document;

    public IngestionJobEventHandler(final DocumentProcessing document) {
        this.document = document;
    }

    @EventListener
    void handle(final IngestionJobStarted event) {
        document.process(StartDocumentProcessing.issue(event.jobId(), event.knowledgeBaseId(), event.documentId()));
    }

}
