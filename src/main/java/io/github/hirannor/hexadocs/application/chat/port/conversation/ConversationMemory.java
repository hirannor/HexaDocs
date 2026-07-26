package io.github.hirannor.hexadocs.application.chat.port.conversation;

import java.util.List;

/**
 * Provides access to persistent conversation history.
 *
 * <p>The implementation is responsible for storing and retrieving messages
 * associated with a conversation identifier. The application layer does not
 * know how the conversation is persisted.</p>
 */
public interface ConversationMemory {

    /**
     * Retrieves the conversation history for the given conversation.
     *
     * @param conversationId unique identifier of the conversation
     * @return previously stored messages, or an empty list if no history exists
     */
    List<ConversationMessage> get(String conversationId);


    /**
     * Adds messages to an existing conversation history.
     *
     * <p>The messages should be appended in chronological order.</p>
     *
     * @param conversationId unique identifier of the conversation
     * @param messages       messages to persist
     */
    void add(String conversationId, List<ConversationMessage> messages);
}