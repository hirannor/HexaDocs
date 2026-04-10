package java.io.github.hirannor.hexadocs.application.knowledgebase.service;

import org.springframework.stereotype.Service;

import java.io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseCreation;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;

@Service
class KnowledgeBaseService implements KnowledgeBaseCreation {
    private final KnowledgeBaseRepository knowledgeBases;

    KnowledgeBaseService(final KnowledgeBaseRepository knowledgeBases) {
        this.knowledgeBases = knowledgeBases;
    }

    @Override
    public KnowledgeBaseId create(final CreateKnowledgeBase command) {
        return null;
    }
}
