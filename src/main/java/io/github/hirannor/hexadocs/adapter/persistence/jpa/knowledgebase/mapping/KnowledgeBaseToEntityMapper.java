package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.KnowledgeBaseEntity;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;

import java.util.function.Function;

public class KnowledgeBaseToEntityMapper implements Function<KnowledgeBase, KnowledgeBaseEntity> {

    @Override
    public KnowledgeBaseEntity apply(KnowledgeBase knowledgeBase) {
        if (knowledgeBase == null) return null;

        final KnowledgeBaseEntity entity = new KnowledgeBaseEntity();
        entity.setKnowledgeBaseId(knowledgeBase.id().asText());
        entity.setName(knowledgeBase.name());

        return entity;
    }
}
