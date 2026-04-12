package io.github.hirannor.hexadocs.adapter.chunking.simple;

import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.TextChunker;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class SimpleTextChunker implements TextChunker {

    private static final int MAX_CHUNK_SIZE = 1200;

    @Override
    public List<Chunk> chunk(final String text, final DocumentId documentId) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        final String normalized = text.replace("\r\n", "\n").trim();

        String[] parts = normalized.split("\\n\\n+");

        if (parts.length == 1) {
            parts = normalized.split("\\. ");
        }

        final List<Chunk> chunks = new ArrayList<>();
        final StringBuilder buffer = new StringBuilder();
        int order = 0;

        for (final String part : parts) {

            final String clean = part.trim();
            if (clean.isEmpty()) continue;

            if (clean.length() > MAX_CHUNK_SIZE) {

                flush(buffer, chunks, documentId, order++);
                buffer.setLength(0);

                int start = 0;
                while (start < clean.length()) {

                    int end = Math.min(start + MAX_CHUNK_SIZE, clean.length());
                    String slice = clean.substring(start, end).trim();

                    if (!slice.isEmpty()) {
                        chunks.add(createChunk(slice, documentId, order++));
                    }

                    start = end;
                }

                continue;
            }

            if (!buffer.isEmpty() &&
                    buffer.length() + clean.length() > MAX_CHUNK_SIZE) {

                flush(buffer, chunks, documentId, order++);
                buffer.setLength(0);
            }

            if (!buffer.isEmpty()) {
                buffer.append("\n\n");
            }

            buffer.append(clean);
        }

        flush(buffer, chunks, documentId, order);

        return chunks;
    }

    private Chunk createChunk(final String content, final DocumentId documentId, final int order) {
        final String chunkId = documentId.asText() + ":" + order;
        final String chunkHash = Integer.toHexString(content.hashCode());

        return Chunk.of(content, order, chunkId, chunkHash);
    }

    private void flush(
            final StringBuilder buffer,
            final List<Chunk> chunks,
            final DocumentId documentId,
            final int order
    ) {
        if (buffer.isEmpty()) return;

        final String content = buffer.toString().trim();
        if (!content.isEmpty()) {
            chunks.add(createChunk(content, documentId, order));
        }
    }
}