package java.io.github.hirannor.hexadocs.application.knowledgebase.usecase;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public interface KnowledgeBaseCreation {
    KnowledgeBaseId create(final CreateKnowledgeBase command);
}
