package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentEntity;
import io.github.hirannor.hexadocs.domain.document.Document;

import java.time.Instant;
import java.util.function.Function;

public class DocumentToEntityMapper implements Function<Document, DocumentEntity> {
    @Override
    public DocumentEntity apply(final Document domain) {
        if (domain == null) return null;

        final DocumentEntity entity = new DocumentEntity();
        entity.setName(domain.name());
        entity.setDocumentId(domain.id().asText());
        entity.setKnowledgeBaseId(domain.knowledgeBaseId().asText());
        entity.setFileReference(domain.fileReference().value());
        entity.setCreatedAt(Instant.now());

        return entity;
    }
}
