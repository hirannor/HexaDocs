package io.github.hirannor.hexadocs.domain.ingestionjob;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public class IngestionJobBuilder {
    private IngestionJobId id;
    private DocumentId documentId;
    private KnowledgeBaseId knowledgeBaseId;
    private JobStatus status;
    private String error;

    public IngestionJobBuilder id(final IngestionJobId id) {
        this.id = id;
        return this;
    }

    public IngestionJobBuilder documentId(final DocumentId documentId) {
        this.documentId = documentId;
        return this;
    }

    public IngestionJobBuilder kbId(final KnowledgeBaseId knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
        return this;
    }

    public IngestionJobBuilder status(final JobStatus status) {
        this.status = status;
        return this;
    }

    public IngestionJobBuilder error(final String error) {
        this.error = error;
        return this;
    }

    public IngestionJob create() {
        return new IngestionJob(id, documentId, knowledgeBaseId, status, error);
    }
}