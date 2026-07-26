package io.github.hirannor.hexadocs.application.knowledgebase.service;

import io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseCreation;
import io.github.hirannor.hexadocs.application.knowledgebase.usecase.KnowledgeBaseDisplaying;
import io.github.hirannor.hexadocs.domain.knowledgebase.CreateKnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class KnowledgeBaseService implements KnowledgeBaseCreation, KnowledgeBaseDisplaying {
    private final KnowledgeBaseRepository knowledgeBases;
    private final MessagePublisher messages;

    KnowledgeBaseService(final KnowledgeBaseRepository knowledgeBases, final MessagePublisher messages) {
        this.knowledgeBases = knowledgeBases;
        this.messages = messages;
    }

    @Override
    public KnowledgeBaseId create(final CreateKnowledgeBase command) {
        final KnowledgeBase knowledgeBase = KnowledgeBase.create(command);

        knowledgeBases.save(knowledgeBase);

        knowledgeBase.events()
                .forEach(messages::publish);
        knowledgeBase.clearEvents();

        return knowledgeBase.id();
    }

    @Override
    public List<KnowledgeBase> displayAll() {
        return knowledgeBases.findAll();
    }
}
