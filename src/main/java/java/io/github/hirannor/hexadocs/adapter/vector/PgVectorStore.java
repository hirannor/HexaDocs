package java.io.github.hirannor.hexadocs.adapter.vector;

import java.io.github.hirannor.hexadocs.application.document.port.VectorDocument;
import java.io.github.hirannor.hexadocs.application.document.port.VectorQuery;
import java.io.github.hirannor.hexadocs.application.document.port.VectorSearchResult;
import java.io.github.hirannor.hexadocs.application.document.port.VectorStore;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.util.List;

class PgVectorStore implements VectorStore {
    PgVectorStore() {}

    @Override
    public void store(final List<VectorDocument> documents) {

    }

    @Override
    public List<VectorSearchResult> search(final VectorQuery query) {
        return List.of();
    }

    @Override
    public void deleteByDocumentId(final DocumentId documentId) {

    }
}
