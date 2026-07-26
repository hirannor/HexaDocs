package io.github.hirannor.hexadocs.application.chat.usecase;

import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.conversation.CreateConversation;

public interface ConversationCreation {

    Conversation create(CreateConversation command);
}