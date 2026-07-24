package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import java.util.Map;

public interface ContextChunk {

    String id();

    String content();

    Map<String, Object> metadata();
}
