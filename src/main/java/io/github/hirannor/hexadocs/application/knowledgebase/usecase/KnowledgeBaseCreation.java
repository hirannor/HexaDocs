package io.github.hirannor.hexadocs.application.knowledgebase.usecase;

import io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public interface KnowledgeBaseCreation {
    KnowledgeBaseId create(final CreateKnowledgeBase command);
}
