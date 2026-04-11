package io.github.hirannor.hexadocs.application.document.port;

import java.util.Objects;

public record Chunk(
        String content,
        int order
) {
    public Chunk {
        Objects.requireNonNull(content);
        if (content.isBlank()) {
            throw new IllegalArgumentException("Chunk content cannot be blank");
        }
    }
}