package io.github.hirannor.hexadocs.application.document.usecase;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.UploadDocument;

public interface DocumentUploading {
    DocumentId upload(final UploadDocument command, final byte[] content);
}
