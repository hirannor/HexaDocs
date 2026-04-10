package java.io.github.hirannor.hexadocs.domain.document;

import java.util.Arrays;

public record Embedding(float[] vector) {

    public Embedding {
        if (vector == null || vector.length == 0) {
            throw new IllegalArgumentException("Embedding cannot be empty");
        }

        vector = Arrays.copyOf(vector, vector.length);
    }

    public float[] vector() {
        return Arrays.copyOf(vector, vector.length);
    }
}