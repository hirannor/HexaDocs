package io.github.hirannor.hexadocs.adapter.web.rest.document;

import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;

import java.util.function.Function;

class DocumentLanguageModelToDomainMapper implements Function<DocumentLanguageModel, DocumentLanguage> {

    DocumentLanguageModelToDomainMapper() {}

    @Override
    public DocumentLanguage apply(final DocumentLanguageModel model) {
        if (model == null) return null;

        return switch (model) {
            case ENGLISH -> DocumentLanguage.ENGLISH;
            case HUNGARIAN -> DocumentLanguage.HUNGARIAN;
        };
    }
}
