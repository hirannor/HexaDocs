package io.github.hirannor.hexadocs.domain.ingestionjob;

import java.util.UUID;

public record IngestionJobId(UUID uniqueComponent) {
    public IngestionJobId {
        if (uniqueComponent == null)
            throw new IllegalArgumentException("JobId can't be null");
    }

    public static IngestionJobId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                    "JobId can't be null or empty"
            );
        }

        return new IngestionJobId(UUID.fromString(source));
    }

    public static IngestionJobId generate() {
        return new IngestionJobId(UUID.randomUUID());
    }

    public String asText() {
        return this.uniqueComponent.toString();
    }
}