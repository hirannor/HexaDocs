package io.github.hirannor.hexadocs.application.document.port;

import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.util.Optional;

public interface DocumentStorage {
    void store(final DocumentId documentId, final DocumentFile document);

    Optional<byte[]> loadById(final DocumentId documentId);

    void delete(final DocumentId documentId);

    boolean exists(final DocumentId documentId);
}
