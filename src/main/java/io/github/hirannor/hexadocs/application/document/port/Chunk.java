package io.github.hirannor.hexadocs.application.document.port;

import java.util.Objects;

public record Chunk(
        String content,
        int order,
        String chunkId,
        String chunkHash
) {
    public Chunk {
        Objects.requireNonNull(content);
        if (content.isBlank()) {
            throw new IllegalArgumentException("Chunk content cannot be blank");
        }
    }

    public static Chunk of(final String content, final int order, final String chunkId, final String chunkHash) {
        return new Chunk(content, order, chunkId, chunkHash);
    }
}