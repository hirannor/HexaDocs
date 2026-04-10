package java.io.github.hirannor.hexadocs.application.document.port;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;

public interface DocumentStorage {
    void store(final DocumentId documentId, final byte[] content, final String contentType);

    byte[] load(final DocumentId documentId);

    void delete(final DocumentId documentId);

    boolean exists(final DocumentId documentId);
}
