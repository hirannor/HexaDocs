package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import java.util.List;

/**
 * Provides access to the knowledge store used for vector-based retrieval.
 */
public interface KnowledgeStore {

    /**
     * Stores vectorized documents in the knowledge store.
     *
     * @param documents the vector documents to store
     */
    void store(final List<VectorDocument> documents);


    /**
     * Searches the knowledge store for documents relevant to the specified query.
     *
     * @param query the vector search query
     * @return the matching vector search results
     */
    List<VectorSearchResult> search(final VectorQuery query);

}
