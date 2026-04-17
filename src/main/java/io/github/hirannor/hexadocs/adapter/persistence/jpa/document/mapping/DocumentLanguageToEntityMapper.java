package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.DocumentLanguageEntity;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;

import java.util.function.Function;

public class DocumentLanguageToEntityMapper  implements Function<DocumentLanguage, DocumentLanguageEntity> {

    public DocumentLanguageToEntityMapper() {}

    @Override
    public DocumentLanguageEntity apply(final DocumentLanguage domain) {
        if (domain == null) return null;

        return switch (domain) {
            case ENGLISH -> DocumentLanguageEntity.ENGLISH;
            case HUNGARIAN -> DocumentLanguageEntity.HUNGARIAN;
        };
    }
}

