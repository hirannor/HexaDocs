package java.io.github.hirannor.hexadocs.domain.document;

import java.util.List;

public record Embedding(List<Double> vector) {
    public Embedding {
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("Embedding cannot be empty");
        }
        vector = List.copyOf(vector);
    }
}