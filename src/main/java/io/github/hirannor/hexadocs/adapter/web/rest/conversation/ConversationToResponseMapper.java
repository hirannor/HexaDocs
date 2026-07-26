package io.github.hirannor.hexadocs.adapter.web.rest.conversation;


import io.github.hirannor.hexadocs.domain.conversation.Conversation;

import java.util.function.Function;

public class ConversationToResponseMapper implements Function<Conversation, ConversationResponse> {

    @Override
    public ConversationResponse apply(final Conversation domain) {
        if (domain == null) {
            return null;
        }

        return new ConversationResponse(domain.id()
                .asText(), domain.title(), domain.createdAt());
    }
}