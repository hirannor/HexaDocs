package java.io.github.hirannor.hexadocs.adapter.file;

import java.io.github.hirannor.hexadocs.application.document.port.DocumentStorage;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;

class FileSystemDocumentStorage implements DocumentStorage {

    FileSystemDocumentStorage() {}

    @Override
    public void store(final DocumentId documentId, final byte[] content, final String contentType) {

    }

    @Override
    public byte[] load(final DocumentId documentId) {
        return new byte[0];
    }

    @Override
    public void delete(final DocumentId documentId) {

    }

    @Override
    public boolean exists(final DocumentId documentId) {
        return false;
    }
}
