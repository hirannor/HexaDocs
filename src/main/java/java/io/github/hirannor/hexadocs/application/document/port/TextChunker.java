package java.io.github.hirannor.hexadocs.application.document.port;

import java.util.List;

public interface TextChunker {
    List<Chunk> chunk(final String text);
}
