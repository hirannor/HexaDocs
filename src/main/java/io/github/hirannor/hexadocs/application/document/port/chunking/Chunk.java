package io.github.hirannor.hexadocs.application.document.port.chunking;

import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractionMethod;
import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

public record Chunk(DocumentId documentId, int pageNumber, String content, int order, ExtractionMethod extractionMethod,
                    String id, String contentHash) {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int ID_HASH_LENGTH = 12;

    public Chunk {
        Objects.requireNonNull(documentId, "Document ID cannot be null");

        Objects.requireNonNull(content, "Chunk content cannot be null");

        Objects.requireNonNull(extractionMethod, "Extraction method cannot be null");

        Objects.requireNonNull(id, "Chunk ID cannot be null");

        Objects.requireNonNull(contentHash, "Chunk hash cannot be null");

        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than zero");
        }

        if (content.isBlank()) {
            throw new IllegalArgumentException("Chunk content cannot be blank");
        }

        if (order < 0) {
            throw new IllegalArgumentException("Chunk order cannot be negative");
        }

        if (id.isBlank()) {
            throw new IllegalArgumentException("Chunk ID cannot be blank");
        }

        if (contentHash.isBlank()) {
            throw new IllegalArgumentException("Chunk hash cannot be blank");
        }
    }

    public static Chunk of(final DocumentId documentId, final int pageNumber, final String content, final int order,
                           final ExtractionMethod extractionMethod) {
        final String contentHash = calculateHash(content);

        return new Chunk(documentId, pageNumber, content, order, extractionMethod,
                createId(documentId, pageNumber, order, contentHash), contentHash);
    }

    private static String createId(final DocumentId documentId, final int pageNumber, final int order,
                                   final String contentHash) {
        return "%s:%d:%d:%s".formatted(documentId.asText(), pageNumber, order,
                contentHash.substring(0, ID_HASH_LENGTH));
    }

    private static String calculateHash(final String content) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

            final byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash algorithm is not available: " + HASH_ALGORITHM, e);
        }
    }
}
