package java.io.github.hirannor.hexadocs.domain.document;

import java.util.UUID;

public record DocumentId(UUID uniqueComponent) {
    public DocumentId {
        if (uniqueComponent == null)
            throw new IllegalArgumentException("DocumentId can't be null");
    }

    public static DocumentId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                "DocumentId can't be null or empty"
            );
        }

        return new DocumentId(UUID.fromString(source));
    }

    public static DocumentId generate() {
        return new DocumentId(UUID.randomUUID());
    }

    public String asText() {
        return this.uniqueComponent.toString();
    }
}