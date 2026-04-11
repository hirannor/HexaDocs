package io.github.hirannor.hexadocs.domain.knowledgebase;

import java.util.Optional;

public interface KnowledgeBaseRepository {

    void save(final KnowledgeBase knowledgeBase);

    Optional<KnowledgeBase> findById(final KnowledgeBaseId id);

    boolean existsById(final KnowledgeBaseId id);

    void deleteById(final KnowledgeBaseId id);
}