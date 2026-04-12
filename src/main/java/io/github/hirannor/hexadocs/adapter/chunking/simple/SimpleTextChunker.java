package io.github.hirannor.hexadocs.adapter.chunking.simple;

import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.TextChunker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class SimpleTextChunker implements TextChunker {

    SimpleTextChunker() {
    }

    @Override
    public List<Chunk> chunk(final String text) {
        return List.of();
    }
}
