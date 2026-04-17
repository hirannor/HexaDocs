package io.github.hirannor.hexadocs.adapter.chunking.npl;

import com.ibm.icu.text.BreakIterator;
import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.ChunkText;
import io.github.hirannor.hexadocs.application.document.port.TextChunker;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Component
class NplTextChunker implements TextChunker {

    private static final int MAX_CHUNK_SIZE = 300;
    private static final int OVERLAP_SENTENCES = 2;

    private final Function<DocumentLanguage, Locale> mapLocale;

    NplTextChunker() {
        this.mapLocale = new DocumentLanguageToLocaleMapper();
    }

    @Override
    public List<Chunk> chunk(final ChunkText command) {
        final List<String> sentences = extractSentences(command.text(), command.language());
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
            result.add(buildChunk(chunkSentences, command.document(), order++));

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

    private List<String> extractSentences(final String text, final DocumentLanguage language) {
        final Locale locale = Optional.ofNullable(language)
                .map(mapLocale)
                .orElse(Locale.getDefault());

        final BreakIterator iterator = BreakIterator.getSentenceInstance(locale);
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