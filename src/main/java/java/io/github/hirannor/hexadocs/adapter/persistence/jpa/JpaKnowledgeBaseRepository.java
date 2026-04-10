package java.io.github.hirannor.hexadocs.adapter.persistence.jpa;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseRepository;
import java.util.Optional;

class JpaKnowledgeBaseRepository implements KnowledgeBaseRepository {

    JpaKnowledgeBaseRepository() {}

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
