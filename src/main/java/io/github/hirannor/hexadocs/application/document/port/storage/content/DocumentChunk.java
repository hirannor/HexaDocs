package io.github.hirannor.hexadocs.application.document.port.storage.content;

import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.ContextChunk;

import java.util.Map;

public record DocumentChunk(String id, String content, Map<String, Object> metadata) implements ContextChunk {
}
