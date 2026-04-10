package java.io.github.hirannor.hexadocs.domain.document;

import java.util.Objects;

public class Chunk {
    private final ChunkId id;
    private final String content;
    private final int order;
    private Embedding embedding;

    public Chunk(final ChunkId id,
                 final String content,
                 final int order) {
        this.id = Objects.requireNonNull(id);
        this.content = Objects.requireNonNull(content);
        this.order = order;
    }

    public ChunkId id() {
        return id;
    }

    public String content() {
        return content;
    }

    public int order() {
        return order;
    }

    public Embedding embedding() {
        return embedding;
    }

    public void attachEmbedding(final Embedding embedding) {
        this.embedding = Objects.requireNonNull(embedding);
    }
}