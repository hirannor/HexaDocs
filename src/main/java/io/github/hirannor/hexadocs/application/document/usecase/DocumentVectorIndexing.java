package io.github.hirannor.hexadocs.application.document.usecase;

/**
 * Provides the use case for indexing document content in a vector index.
 */
public interface DocumentVectorIndexing {

    /**
     * Indexes the specified document.
     *
     * @param command the command containing the document data required
     *                for vector indexing
     */
    void index(final IndexDocument command);
}
