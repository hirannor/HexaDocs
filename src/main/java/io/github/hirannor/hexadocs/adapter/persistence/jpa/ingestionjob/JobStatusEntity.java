package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob;

import java.util.Objects;

public enum JobStatusEntity {

    PENDING("PENDING"),
    RUNNING("RUNNING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String dbRepresentation;

    JobStatusEntity(final String dbRepresentation) {
        this.dbRepresentation = dbRepresentation;
    }

    public static JobStatusEntity from(final String text) {
        Objects.requireNonNull(text);

        for (final JobStatusEntity status : JobStatusEntity.values()) {
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