package io.github.hirannor.hexadocs.application.knowledgebase.usecase;

import io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

/**
 * Provides the use case for creating a knowledge base.
 */
public interface KnowledgeBaseCreation {

    /**
     * Creates a knowledge base.
     *
     * @param command the command containing the information required
     *                to create the knowledge base
     * @return the identifier of the created knowledge base
     */
    KnowledgeBaseId create(final CreateKnowledgeBase command);
}
