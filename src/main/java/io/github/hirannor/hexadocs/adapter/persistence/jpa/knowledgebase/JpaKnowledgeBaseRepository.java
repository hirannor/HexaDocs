package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.mapping.KnowledgeBaseEntityToDomainMapper;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.mapping.KnowledgeBaseToEntityMapper;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
class JpaKnowledgeBaseRepository implements KnowledgeBaseRepository {
    private final Function<KnowledgeBaseEntity, KnowledgeBase> mapToDomain;
    private final Function<KnowledgeBase, KnowledgeBaseEntity> mapToEntity;

    private final KnowledgeBaseSpringDataJpaRepository knowledgeBases;

    JpaKnowledgeBaseRepository(final KnowledgeBaseSpringDataJpaRepository knowledgeBases) {
        this.knowledgeBases = knowledgeBases;
        this.mapToDomain = new KnowledgeBaseEntityToDomainMapper();
        this.mapToEntity = new KnowledgeBaseToEntityMapper();
    }

    @Override
    public void save(final KnowledgeBase knowledgeBase) {
        knowledgeBases.save(mapToEntity.apply(knowledgeBase));
    }

    @Override
    public Optional<KnowledgeBase> findById(final KnowledgeBaseId id) {
        return knowledgeBases.findByKnowledgeBaseId(id.asText())
                .map(mapToDomain);
    }

    @Override
    public List<KnowledgeBase> findAll() {
        return knowledgeBases.findAll()
                .stream()
                .map(mapToDomain)
                .toList();
    }

}