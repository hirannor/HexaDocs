package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface KnowledgeBaseSpringDataJpaRepository extends Repository<KnowledgeBaseEntity, Long> {
    void save(final KnowledgeBaseEntity knowledgeBase);

    Optional<KnowledgeBaseEntity> findByKnowledgeBaseId(final String id);

    boolean existsByKnowledgeBaseId(final String id);

    void deleteByKnowledgeBaseId(final String id);
}
