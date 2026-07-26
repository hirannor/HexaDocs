package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMemory;
import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import io.github.hirannor.hexadocs.application.chat.usecase.ConversationHistoryDisplaying;
import io.github.hirannor.hexadocs.domain.conversation.ConversationId;
import io.github.hirannor.hexadocs.domain.conversation.ConversationNotFound;
import io.github.hirannor.hexadocs.domain.conversation.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ConversationHistoryService implements ConversationHistoryDisplaying {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationHistoryService.class);

    private final ConversationRepository conversationRepository;
    private final ConversationMemory conversationMemory;

    public ConversationHistoryService(final ConversationRepository conversationRepository,
            final ConversationMemory conversationMemory) {

        this.conversationRepository = conversationRepository;
        this.conversationMemory = conversationMemory;
    }

    @Override
    public List<ConversationMessage> displayAllById(final ConversationId conversationId) {
        LOGGER.info("Retrieving conversation history | conversationId={}", conversationId);

        conversationRepository.findById(conversationId)
                .orElseThrow(
                        () -> new ConversationNotFound("Conversation not found with id: " + conversationId.asText()));

        final List<ConversationMessage> messages = conversationMemory.get(conversationId.asText());

        LOGGER.info("Conversation history retrieved | conversationId={} | messageCount={}", conversationId,
                messages.size());

        return messages;
    }
}