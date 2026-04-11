package io.github.hirannor.hexadocs.application.ingestionpipeline;


import io.github.hirannor.hexadocs.application.document.service.DocumentTextExtracted;
import io.github.hirannor.hexadocs.application.document.usecase.*;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobCompleting;
import io.github.hirannor.hexadocs.application.ingestionjob.usecase.IngestionJobStarting;
import io.github.hirannor.hexadocs.domain.document.events.DocumentRegistered;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobStarted;
import io.github.hirannor.hexadocs.domain.ingestionjob.StartIngestionJob;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class IngestionPipelineEventHandler {
    private final IngestionJobStarting ingestionJob;
    private final IngestionJobCompleting ingestionJobCompleting;
    private final DocumentVectorIndexing documentVectorIndexing;
    private final DocumentTextExtracting document;

    public IngestionPipelineEventHandler(final IngestionJobStarting ingestionJob,
                                         final IngestionJobCompleting ingestionJobCompleting,
                                         final DocumentVectorIndexing documentVectorIndexing,
                                         final DocumentTextExtracting document) {
        this.ingestionJob = ingestionJob;
        this.ingestionJobCompleting = ingestionJobCompleting;
        this.documentVectorIndexing = documentVectorIndexing;
        this.document = document;
    }

    @EventListener
    void handle(final DocumentRegistered event) {
        ingestionJob.start(StartIngestionJob.issue(event.documentId(), event.knowledgeBaseId()));
    }

    @EventListener
    void handle(final IngestionJobStarted event) {
        document.extract(ExtractDocumentText.issue(event.jobId(), event.documentId(), event.knowledgeBaseId()));
    }

    @EventListener
    void handle(final DocumentTextExtracted event) {
        documentVectorIndexing.index(IndexDocument.issue(event.ingestionJobId(), event.knowledgeBaseId(), event.document()));
    }

    @EventListener
    void handle(final DocumentVectorIndexed event) {
        ingestionJobCompleting.complete(event.ingestionJobId());
    }

}
