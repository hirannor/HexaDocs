package io.github.hirannor.hexadocs.domain.conversation;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository {

    Conversation save(final Conversation conversation);

    Optional<Conversation> findById(final ConversationId id);

    List<Conversation> findByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId);
}