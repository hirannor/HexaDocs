package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import java.util.Objects;

public enum ExtractionMethodEntity {
    TEXT_LAYER("Text layer"), OCR("OCR");

    private final String dbRepresentation;

    ExtractionMethodEntity(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static ExtractionMethodEntity from(final String text) {
        Objects.requireNonNull(text);

        for (final ExtractionMethodEntity method : ExtractionMethodEntity.values()) {
            if (method.dbRepresentation.equalsIgnoreCase(text)) {
                return method;
            }
        }

        throw new IllegalArgumentException(String.format("Unexpected value %s", text));
    }

    public String dbRepresentation() {
        return dbRepresentation;
    }
}
