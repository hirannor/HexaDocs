package java.io.github.hirannor.hexadocs.domain.document;

import java.util.UUID;

public record ChunkId(UUID uniqueComponent) {
    public ChunkId {
        if (uniqueComponent == null)
            throw new IllegalArgumentException("ChunkId can't be null");
    }

    public static ChunkId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("ChunkId can't be null or empty"
            );
        }

        return new ChunkId(UUID.fromString(source));
    }

    public static ChunkId generate() {
        return new ChunkId(UUID.randomUUID());
    }

    public String asText() {
        return this.uniqueComponent.toString();
    }
}