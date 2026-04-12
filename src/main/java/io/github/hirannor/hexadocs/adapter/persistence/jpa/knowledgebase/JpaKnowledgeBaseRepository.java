package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.mapping.KnowledgeBaseEntityToDomainMapper;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.mapping.KnowledgeBaseToEntityMapper;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Function;

@Repository
class JpaKnowledgeBaseRepository implements KnowledgeBaseRepository {

    private final Function<KnowledgeBaseEntity, KnowledgeBase> mapToDomain;
    private final Function<KnowledgeBase, KnowledgeBaseEntity> mapToEntity;

    private final KnowledgeBaseSpringDataJpaRepository knowledgebases;

    JpaKnowledgeBaseRepository(final KnowledgeBaseSpringDataJpaRepository knowledgebases) {
        this.knowledgebases = knowledgebases;
        this.mapToDomain = new KnowledgeBaseEntityToDomainMapper();
        this.mapToEntity = new KnowledgeBaseToEntityMapper();
    }

    @Override
    public void save(final KnowledgeBase knowledgeBase) {
        knowledgebases.save(mapToEntity.apply(knowledgeBase));
    }

    @Override
    public Optional<KnowledgeBase> findById(final KnowledgeBaseId id) {
        return knowledgebases.findByKnowledgeBaseId(id.asText())
                .map(mapToDomain);
    }

    @Override
    public boolean existsById(final KnowledgeBaseId id) {
        return knowledgebases.existsByKnowledgeBaseId(id.asText());
    }

    @Override
    public void deleteById(final KnowledgeBaseId id) {
        knowledgebases.deleteByKnowledgeBaseId(id.asText());
    }
}