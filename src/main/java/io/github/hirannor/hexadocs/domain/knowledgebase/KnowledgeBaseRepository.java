package io.github.hirannor.hexadocs.domain.knowledgebase;

import java.util.List;
import java.util.Optional;

public interface KnowledgeBaseRepository {

    void save(final KnowledgeBase knowledgeBase);

    Optional<KnowledgeBase> findById(final KnowledgeBaseId id);

    List<KnowledgeBase> findAll();

}