package io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation.mapping.ConversationEntityToDomainMapper;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation.mapping.ConversationToEntityMapper;
import io.github.hirannor.hexadocs.domain.conversation.Conversation;
import io.github.hirannor.hexadocs.domain.conversation.ConversationId;
import io.github.hirannor.hexadocs.domain.conversation.ConversationRepository;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
class ConversationSpringDataJpaRepository implements ConversationRepository {

    private final Function<ConversationEntity, Conversation> mapToDomain;
    private final Function<Conversation, ConversationEntity> mapToEntity;

    private final ConversationJpaRepository conversations;

    ConversationSpringDataJpaRepository(final ConversationJpaRepository conversations) {
        this.conversations = conversations;
        this.mapToDomain = new ConversationEntityToDomainMapper();
        this.mapToEntity = new ConversationToEntityMapper();
    }

    @Override
    public Conversation save(final Conversation conversation) {
        final ConversationEntity toPersist = mapToEntity.apply(conversation);
        final ConversationEntity saved = conversations.save(toPersist);

        return mapToDomain.apply(saved);
    }

    @Override
    public Optional<Conversation> findById(final ConversationId id) {
        return conversations.findByConversationId(id.asText())
                .map(mapToDomain);
    }

    @Override
    public List<Conversation> findByKnowledgeBaseId(final KnowledgeBaseId knowledgeBaseId) {
        return conversations.findAllByKnowledgeBaseId(knowledgeBaseId.asText())
                .stream()
                .map(mapToDomain)
                .toList();
    }

}