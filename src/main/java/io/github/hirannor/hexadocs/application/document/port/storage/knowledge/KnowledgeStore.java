package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import java.util.List;

public interface KnowledgeStore {
    void store(final List<VectorDocument> documents);

    List<VectorSearchResult> search(final VectorQuery query);

}
