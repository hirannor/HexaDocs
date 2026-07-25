package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.ContextChunk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class DefaultContextBuilder implements ContextBuilder {

    private static final int MAX_CONTEXT_WINDOW_CHARS = 8_000;

    private static final String PAGE_NUMBER_METADATA_KEY = "pageNumber";
    private static final String CHUNK_ORDER_METADATA_KEY = "chunkOrder";

    @Override
    public String build(final List<? extends ContextChunk> chunks) {
        final StringBuilder context = new StringBuilder();

        for (int i = 0; i < chunks.size(); i++) {
            final String source = formatSource(i, chunks.get(i));
            final int separatorLength = context.isEmpty() ? 0 : 5;

            if (context.length() + separatorLength + source.length() > MAX_CONTEXT_WINDOW_CHARS) {
                break;
            }

            if (!context.isEmpty()) {
                context.append("\n---\n");
            }

            context.append(source);
        }

        return context.toString();
    }

    @Override
    public List<String> buildWindows(final List<? extends ContextChunk> chunks) {
        final List<String> windows = new ArrayList<>();
        final StringBuilder current = new StringBuilder();

        for (int i = 0; i < chunks.size(); i++) {
            final String source = formatSource(i, chunks.get(i));
            final int separatorLength = current.isEmpty() ? 0 : 5;

            if (!current.isEmpty() && current.length() + separatorLength + source.length() > MAX_CONTEXT_WINDOW_CHARS) {
                windows.add(current.toString());
                current.setLength(0);
            }

            if (!current.isEmpty()) {
                current.append("\n---\n");
            }

            current.append(source);
        }

        if (!current.isEmpty()) {
            windows.add(current.toString());
        }

        return windows;
    }

    private String formatSource(final int index, final ContextChunk chunk) {
        return """
                [Source %d]
                [Page: %s]
                [Chunk: %s]

                %s
                """.formatted(index + 1, extractMetadata(chunk, PAGE_NUMBER_METADATA_KEY),
                extractMetadata(chunk, CHUNK_ORDER_METADATA_KEY), chunk.content());
    }

    private Object extractMetadata(final ContextChunk chunk, final String key) {
        return chunk.metadata().getOrDefault(key, "unknown");
    }
}