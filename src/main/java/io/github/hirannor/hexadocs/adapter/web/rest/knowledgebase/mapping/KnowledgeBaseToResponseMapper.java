package io.github.hirannor.hexadocs.adapter.web.rest.knowledgebase.mapping;

import io.github.hirannor.hexadocs.adapter.web.rest.knowledgebase.KnowledgeBaseResponse;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBase;

import java.util.function.Function;

public class KnowledgeBaseToResponseMapper implements Function<KnowledgeBase, KnowledgeBaseResponse> {

    public KnowledgeBaseToResponseMapper() {
    }

    @Override
    public KnowledgeBaseResponse apply(final KnowledgeBase domain) {
        if (domain == null) return null;

        return new KnowledgeBaseResponse(domain.id()
                .asText(), domain.name());
    }
}
