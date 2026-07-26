package io.github.hirannor.hexadocs.adapter.web.rest.conversation;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;

import java.util.function.Function;

public class ConversationMessageToResponseMapper implements Function<ConversationMessage, ConversationMessageResponse> {

    public ConversationMessageToResponseMapper() {
    }

    @Override
    public ConversationMessageResponse apply(final ConversationMessage message) {
        if (message == null) {
            return null;
        }

        return new ConversationMessageResponse(message.role()
                .name(), message.content());
    }
}