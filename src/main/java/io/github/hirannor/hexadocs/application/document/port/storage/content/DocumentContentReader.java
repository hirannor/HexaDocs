package io.github.hirannor.hexadocs.application.document.port.storage.content;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;


/**
 * Provides access to the textual content of documents stored in a knowledge base.
 */
public interface DocumentContentReader {

    /**
     * Reads all document chunks belonging to the specified knowledge base.
     *
     * @param knowledgeBaseId the identifier of the knowledge base
     * @return all document chunks belonging to the knowledge base
     */
    List<DocumentChunk> readAll(KnowledgeBaseId knowledgeBaseId);
}
