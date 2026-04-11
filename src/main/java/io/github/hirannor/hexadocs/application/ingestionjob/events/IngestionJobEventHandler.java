package io.github.hirannor.hexadocs.application.ingestionjob.events;

import io.github.hirannor.hexadocs.application.document.usecase.DocumentTextExtracting;
import io.github.hirannor.hexadocs.application.document.usecase.ExtractDocumentText;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobStarted;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class IngestionJobEventHandler {
    private final DocumentTextExtracting document;

    public IngestionJobEventHandler(final DocumentTextExtracting document) {
        this.document = document;
    }

    @EventListener
    void handle(final IngestionJobStarted event) {
        document.extract(ExtractDocumentText.issue(event.jobId(), event.documentId(), event.knowledgeBaseId()));
    }

}
