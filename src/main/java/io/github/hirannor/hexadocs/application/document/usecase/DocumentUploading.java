package io.github.hirannor.hexadocs.application.document.usecase;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.UploadDocument;

/**
 * Provides the use case for uploading documents.
 */
public interface DocumentUploading {

    /**
     * Uploads the specified document content.
     *
     * @param command the command containing document metadata
     * @param content the binary content of the document
     * @return the identifier of the uploaded document
     */
    DocumentId upload(final UploadDocument command, final byte[] content);
}
