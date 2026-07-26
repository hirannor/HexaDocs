package io.github.hirannor.hexadocs.application.chat.usecase;

import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;

public interface ConversationDisplaying {

    List<Conversation> displayAllByKnowledgeBaseId(KnowledgeBaseId knowledgeBaseId);
}