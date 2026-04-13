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

    private static final int MAX_CHUNK_SIZE = 300;
    private static final int OVERLAP_SENTENCES = 2;

    @Override
    public List<Chunk> chunk(final String text, final DocumentId documentId) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        final List<String> sentences = extractSentences(text);
        final List<Chunk> result = new ArrayList<>();

        int start = 0;
        int order = 0;

        while (start < sentences.size()) {

            int size = 0;
            int end = start;

            while (end < sentences.size()) {
                int sentenceSize = estimateTokens(sentences.get(end));

                if (size + sentenceSize > MAX_CHUNK_SIZE && end > start) {
                    break;
                }

                size += sentenceSize;
                end++;
            }

            final List<String> chunkSentences = sentences.subList(start, end);
            result.add(buildChunk(chunkSentences, documentId, order++));

            start = Math.max(start + 1, end - OVERLAP_SENTENCES);
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
        final BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.getDefault());
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

    private int estimateTokens(final String text) {
        return Math.max(1, text.length() / 4);
    }
}