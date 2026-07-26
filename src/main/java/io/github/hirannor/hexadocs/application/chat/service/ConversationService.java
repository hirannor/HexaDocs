package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.usecase.ConversationCreation;
import io.github.hirannor.hexadocs.application.chat.usecase.ConversationDisplaying;
import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.conversation.ConversationRepository;
import io.github.hirannor.hexadocs.domain.conversation.CreateConversation;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseNotFound;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConversationService implements ConversationCreation, ConversationDisplaying {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationService.class);

    private final KnowledgeBaseRepository knowledgeBases;
    private final ConversationRepository conversationRepository;

    public ConversationService(final KnowledgeBaseRepository knowledgeBases,
            final ConversationRepository conversationRepository) {

        this.knowledgeBases = knowledgeBases;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Conversation create(final CreateConversation command) {
        LOGGER.info("Creating conversation | knowledgeBaseId={}", command.knowledgeBaseId());

        knowledgeBases.findById(command.knowledgeBaseId())
                .orElseThrow(() -> new KnowledgeBaseNotFound(
                        "Knowledge base not found with id " + command.knowledgeBaseId()
                                .asText()));

        final Conversation conversation = Conversation.create(command);

        final Conversation savedConversation = conversationRepository.save(conversation);

        LOGGER.info("Conversation created | conversationId={} | knowledgeBaseId={}", savedConversation.id(),
                command.knowledgeBaseId());

        return savedConversation;
    }

    @Override
    public List<Conversation> displayAllByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId) {
        LOGGER.info("Retrieving conversations | knowledgeBaseId={}", knowledgeBaseId);

        knowledgeBases.findById(knowledgeBaseId)
                .orElseThrow(() -> new KnowledgeBaseNotFound(
                        "Knowledge base not found with id " + knowledgeBaseId.asText()));

        final List<Conversation> conversations = conversationRepository.findByKnowledgeBaseId(knowledgeBaseId);

        LOGGER.info("Conversations retrieved | knowledgeBaseId={} | count={}", knowledgeBaseId, conversations.size());

        return conversations;
    }
}