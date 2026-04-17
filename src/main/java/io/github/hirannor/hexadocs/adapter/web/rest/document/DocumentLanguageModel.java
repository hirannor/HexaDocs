package io.github.hirannor.hexadocs.adapter.web.rest.document;

import java.util.Objects;

public enum DocumentLanguageModel {

    ENGLISH("English"),
    HUNGARIAN("Hungarian");

    private final String displayValue;

    DocumentLanguageModel(final String displayValue) {
        this.displayValue = displayValue;
    }

    public static DocumentLanguageModel from(final String text) {
        Objects.requireNonNull(text);

        for (final DocumentLanguageModel status : DocumentLanguageModel.values()) {
            if (status.displayValue.equalsIgnoreCase(text)) return status;
        }

        throw new IllegalArgumentException(
                String.format("Unexpected value %s", text)
        );
    }

    public String displayValue() {
        return this.displayValue;
    }

}