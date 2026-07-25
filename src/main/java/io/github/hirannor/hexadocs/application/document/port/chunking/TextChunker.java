package io.github.hirannor.hexadocs.application.document.port.chunking;

import java.util.List;


/**
 * Splits text into smaller chunks for further processing.
 */
public interface TextChunker {

    /**
     * Splits the supplied text into chunks.
     *
     * @param command the command containing the text to chunk
     * @return the resulting text chunks
     */
    List<Chunk> chunk(final ChunkText command);
}
