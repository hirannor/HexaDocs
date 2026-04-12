package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase.KnowledgeBaseEntity;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.function.Function;

public class KnowledgeBaseEntityToDomainMapper implements Function<KnowledgeBaseEntity, KnowledgeBase> {

    @Override
    public KnowledgeBase apply(final KnowledgeBaseEntity knowledgeBaseEntity) {
        if (knowledgeBaseEntity == null) return null;

        return new KnowledgeBase(
                KnowledgeBaseId.from(knowledgeBaseEntity.getKnowledgeBaseId()),
                knowledgeBaseEntity.getName()
        );
    }
}
