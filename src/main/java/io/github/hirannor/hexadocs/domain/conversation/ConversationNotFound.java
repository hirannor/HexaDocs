package io.github.hirannor.hexadocs.domain.conversation;

public class ConversationNotFound extends RuntimeException {
    public ConversationNotFound(final String message) {
        super(message);
    }
}