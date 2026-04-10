package java.io.github.hirannor.hexadocs.adapter.chunk;

import java.io.github.hirannor.hexadocs.application.document.port.TextChunker;
import java.io.github.hirannor.hexadocs.application.document.port.Chunk;
import java.util.List;

class SimpleTextChunker implements TextChunker {

    SimpleTextChunker() {}

    @Override
    public List<Chunk> chunk(final String text) {
        return List.of();
    }
}
