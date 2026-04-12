package io.github.hirannor.hexadocs.application.knowledgebase.service;

import io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseCreation;
import io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class KnowledgeBaseService implements KnowledgeBaseCreation {
    private final KnowledgeBaseRepository knowledgeBases;

    KnowledgeBaseService(final KnowledgeBaseRepository knowledgeBases) {
        this.knowledgeBases = knowledgeBases;
    }

    @Override
    public KnowledgeBaseId create(final CreateKnowledgeBase command) {
        final KnowledgeBase knowledgeBase = KnowledgeBase.create(command);

        knowledgeBases.save(knowledgeBase);

        return knowledgeBase.id();
    }
}
