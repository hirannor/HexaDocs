package io.github.hirannor.hexadocs.adapter.persistence.jpa;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class JpaKnowledgeBaseRepository implements KnowledgeBaseRepository {

    JpaKnowledgeBaseRepository() {
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
