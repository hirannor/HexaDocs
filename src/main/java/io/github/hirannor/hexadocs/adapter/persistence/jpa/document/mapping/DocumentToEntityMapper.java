package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentEntity;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentLanguageEntity;
import io.github.hirannor.hexadocs.domain.document.Document;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;

import java.time.Instant;
import java.util.function.Function;

public class DocumentToEntityMapper implements Function<Document, DocumentEntity> {

    private final Function<DocumentLanguage, DocumentLanguageEntity> mapLanguage;

    public DocumentToEntityMapper() {
        this.mapLanguage = new DocumentLanguageToEntityMapper();
    }

    @Override
    public DocumentEntity apply(final Document domain) {
        if (domain == null) return null;

        final DocumentEntity entity = new DocumentEntity();
        entity.setName(domain.name());
        entity.setDocumentId(domain.id().asText());
        entity.setKnowledgeBaseId(domain.knowledgeBaseId().asText());
        entity.setFileReference(domain.fileReference().value());
        entity.setCreatedAt(Instant.now());
        entity.setLanguage(mapLanguage.apply(domain.language()));

        return entity;
    }
}
