package io.github.hirannor.hexadocs.application.chat.port.conversation;

public record ConversationMessage(ConversationMessageRole role, String content) {

    public static ConversationMessage user(final String content) {
        return new ConversationMessage(ConversationMessageRole.USER, content);
    }

    public static ConversationMessage assistant(final String content) {
        return new ConversationMessage(ConversationMessageRole.ASSISTANT, content);
    }
}