package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import java.util.Map;

public record VectorSearchResult(String id, String content, double score,
                                 Map<String, Object> metadata) implements ContextChunk {
}
