package io.github.hirannor.hexadocs.application.document.port;

import java.util.Map;

public record VectorSearchResult(
        String id,
        String content,
        double score,
        Map<String, Object> metadata
) {
}