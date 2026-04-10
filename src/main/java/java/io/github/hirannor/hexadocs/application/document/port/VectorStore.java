package java.io.github.hirannor.hexadocs.application.document.port;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.util.List;

public interface VectorStore {
    void store(final List<VectorDocument> documents);
    List<VectorSearchResult> search(final VectorQuery query);
    void deleteByDocumentId(final DocumentId documentId);
}
