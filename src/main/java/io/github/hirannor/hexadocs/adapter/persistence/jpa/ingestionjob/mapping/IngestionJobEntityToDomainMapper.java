package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.IngestionJobEntity;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.JobStatusEntity;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJob;
import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.domain.ingestionjob.JobStatus;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.function.Function;

public class IngestionJobEntityToDomainMapper implements Function<IngestionJobEntity, IngestionJob> {

    private final Function<JobStatusEntity, JobStatus> mapToDomain;

    public IngestionJobEntityToDomainMapper() {
        this.mapToDomain = new IngestionJobStatusEntityToDomainMapper();
    }

    @Override
    public IngestionJob apply(final IngestionJobEntity entity) {

        return IngestionJob.empty()
                .id(IngestionJobId.from(entity.getIngestionJobId()))
                .documentId(DocumentId.from(entity.getDocumentId()))
                .kbId(KnowledgeBaseId.from(entity.getKnowledgeBaseId()))
                .status(mapToDomain.apply(entity.status()))
                .create();
    }
}