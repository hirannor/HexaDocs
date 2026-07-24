package io.github.hirannor.hexadocs.application.document.port.chunking;

import java.util.List;

public interface TextChunker {
    List<Chunk> chunk(final ChunkText command);
}
