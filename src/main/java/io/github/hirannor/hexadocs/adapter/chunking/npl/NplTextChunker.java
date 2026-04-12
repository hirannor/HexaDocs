package io.github.hirannor.hexadocs.adapter.chunking.npl;

import com.ibm.icu.text.BreakIterator;
import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.TextChunker;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
class NplTextChunker implements TextChunker {

    private static final int MAX_CHUNK_SIZE = 1200;
    private static final int OVERLAP_SENTENCES = 2;

    @Override
    public List<Chunk> chunk(final String text, final DocumentId documentId) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        final List<String> sentences = extractSentences(text);
        final List<Chunk> result = new ArrayList<>();

        final List<String> window = new ArrayList<>();
        int order = 0;

        for (int i = 0; i < sentences.size(); i++) {
            window.add(sentences.get(i));

            if (estimateSize(window) > MAX_CHUNK_SIZE) {
                window.removeLast();

                result.add(buildChunk(window, documentId, order++));

                final List<String> overlap = new ArrayList<>();

                for (int j = Math.max(0, i - OVERLAP_SENTENCES); j < i; j++) {
                    overlap.add(sentences.get(j));
                }

                overlap.add(sentences.get(i));

                window.clear();
                window.addAll(overlap);
            }
        }

        if (!window.isEmpty()) {
            result.add(buildChunk(window, documentId, order));
        }

        return result;
    }

    private Chunk buildChunk(
            final List<String> sentences,
            final DocumentId documentId,
            final int order
    ) {
        final String content = String.join(" ", sentences).trim();
        return Chunk.of(documentId, content, order);
    }

    private List<String> extractSentences(final String text) {
        final BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        iterator.setText(text);

        final List<String> sentences = new ArrayList<>();

        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {

            String sentence = text.substring(start, end).trim();
            if (!sentence.isBlank()) {
                sentences.add(sentence);
            }
        }

        return sentences;
    }

    private int estimateSize(final List<String> sentences) {
        int size = 0;
        for (String s : sentences) {
            size += s.length();
        }
        return size;
    }
}