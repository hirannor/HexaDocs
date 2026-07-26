package io.github.hirannor.hexadocs.application.chat.usecase;

import io.github.hirannor.hexadocs.application.chat.port.conversation.ConversationMessage;
import io.github.hirannor.hexadocs.domain.conversation.ConversationId;

import java.util.List;

public interface ConversationHistoryDisplaying {

    List<ConversationMessage> displayAllById(ConversationId conversationId);
}