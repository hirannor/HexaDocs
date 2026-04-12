package io.github.hirannor.hexadocs.application.document.port;

import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.util.List;

public interface TextChunker {
    List<Chunk> chunk(final String text, final DocumentId document);
}
