package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import java.util.Objects;

public enum DocumentLanguageEntity {

    ENGLISH("English"),
    HUNGARIAN("Hungarian");

    private final String dbRepresentation;

    DocumentLanguageEntity(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static DocumentLanguageEntity from(final String text) {
        Objects.requireNonNull(text);

        for (final DocumentLanguageEntity status : DocumentLanguageEntity.values()) {
            if (status.dbRepresentation.equalsIgnoreCase(text)) return status;
        }

        throw new IllegalArgumentException(
                String.format("Unexpected value %s", text)
        );
    }

    public String dbRepresentation() {
        return this.dbRepresentation;
    }

}