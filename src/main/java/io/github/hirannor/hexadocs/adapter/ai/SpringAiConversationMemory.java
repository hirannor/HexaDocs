package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMemory;
import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessageRole;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class SpringAiConversationMemory implements ConversationMemory {

    private final ChatMemory chatMemory;

    SpringAiConversationMemory(final ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    @Override
    public List<ConversationMessage> get(final String conversationId) {
        return chatMemory.get(conversationId)
                .stream()
                .map(this::toApplicationMessage)
                .toList();
    }

    @Override
    public void add(final String conversationId, final List<ConversationMessage> messages) {
        chatMemory.add(conversationId, messages.stream()
                .map(this::toSpringAiMessage)
                .toList());
    }

    private ConversationMessage toApplicationMessage(final Message message) {
        return new ConversationMessage(toApplicationRole(message), message.getText());
    }

    private ConversationMessageRole toApplicationRole(final Message message) {
        return switch (message.getMessageType()) {
            case USER -> ConversationMessageRole.USER;
            case ASSISTANT -> ConversationMessageRole.ASSISTANT;
            default -> throw new IllegalArgumentException(
                    "Unsupported conversation message type: " + message.getMessageType());
        };
    }

    private Message toSpringAiMessage(final ConversationMessage message) {
        return switch (message.role()) {
            case USER -> new UserMessage(message.content());

            case ASSISTANT -> new AssistantMessage(message.content());
        };
    }
}