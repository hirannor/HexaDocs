package io.github.hirannor.hexadocs.application.chat.port.query.rewriter;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;

import java.util.List;

/**
 * Rewrites user questions into standalone queries suitable for retrieval.
 *
 * <p>Conversation context is used to resolve ambiguous references such as
 * pronouns ("it", "this", "that") before performing document search.</p>
 */
public interface QueryRewriter {

    /**
     * Creates a standalone search query from the user's question.
     *
     * <p>The implementation should preserve the original intent while using
     * previous conversation messages to resolve missing context.</p>
     *
     * @param question current user question
     * @param history  previous conversation messages
     * @return rewritten query optimized for retrieval
     */
    String rewrite(String question, List<ConversationMessage> history);
}