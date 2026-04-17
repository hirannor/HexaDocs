package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentLanguageEntity;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;

import java.util.function.Function;

public class DocumentLanguageEntityToDomainMapper implements Function<DocumentLanguageEntity, DocumentLanguage> {

    public DocumentLanguageEntityToDomainMapper() {}

    @Override
    public DocumentLanguage apply(final DocumentLanguageEntity entity) {
        if (entity == null) return null;

        return switch (entity) {
            case ENGLISH -> DocumentLanguage.ENGLISH;
            case HUNGARIAN -> DocumentLanguage.HUNGARIAN;
        };
    }
}



