package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaKnowledgeBaseRepository implements KnowledgeBaseRepository {

    private final KnowledgeBaseSpringDataJpaRepository knowledgebases;

    JpaKnowledgeBaseRepository(final KnowledgeBaseSpringDataJpaRepository knowledgebases) {
        this.knowledgebases = knowledgebases;
    }

    @Override
    public void save(KnowledgeBase knowledgeBase) {

    }

    @Override
    public Optional<KnowledgeBase> findById(final KnowledgeBaseId id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final KnowledgeBaseId id) {
        return false;
    }

    @Override
    public void deleteById(final KnowledgeBaseId id) {

    }
}
