package io.github.hirannor.hexadocs.application.document.port.storage.document;

import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.util.Optional;

/**
 * Provides persistent storage for document files.
 */
public interface DocumentStorage {

    /**
     * Stores a document.
     *
     * @param documentId the identifier of the document
     * @param document   the document file to store
     */
    void store(final DocumentId documentId, final DocumentFile document);


    /**
     * Loads a document by its identifier.
     *
     * @param documentId the identifier of the document
     * @return the document content if the document exists
     */
    Optional<byte[]> loadById(final DocumentId documentId);

    /**
     * Deletes a document.
     *
     * @param documentId the identifier of the document to delete
     */
    void delete(final DocumentId documentId);


    /**
     * Determines whether a document exists.
     *
     * @param documentId the identifier of the document
     * @return {@code true} if the document exists; otherwise, {@code false}
     */
    boolean exists(final DocumentId documentId);
}
