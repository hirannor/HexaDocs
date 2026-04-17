package io.github.hirannor.hexadocs.adapter.chunking.npl;

import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;

import java.util.Locale;
import java.util.function.Function;

class DocumentLanguageToLocaleMapper implements Function<DocumentLanguage, Locale> {

    DocumentLanguageToLocaleMapper() {}

    @Override
    public Locale apply(final DocumentLanguage language) {
        if (language == null) return null;

        return switch (language) {
            case ENGLISH -> Locale.ENGLISH;
            case HUNGARIAN -> Locale.forLanguageTag("hu");
        };
    }
}
