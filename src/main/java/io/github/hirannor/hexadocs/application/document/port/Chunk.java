package io.github.hirannor.hexadocs.application.document.port;

import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Objects;

public record Chunk(
        DocumentId documentId,
        String content,
        int order,
        String chunkId,
        String chunkHash
) {

    public Chunk {
        Objects.requireNonNull(documentId);
        Objects.requireNonNull(content);

        if (content.isBlank()) {
            throw new IllegalArgumentException("Chunk content cannot be blank");
        }
    }

    public static Chunk of(
            DocumentId documentId,
            String content,
            int order
    ) {
        final String hash = sha256(content);
        final String chunkId = deterministicId(documentId, order, hash);

        return new Chunk(
                documentId,
                content,
                order,
                chunkId,
                hash
        );
    }

    private static String deterministicId(DocumentId docId, int order, String hash) {
        return docId.asText() + ":" + order + ":" + hash.substring(0, 12);
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash chunk content", e);
        }
    }
}