package io.github.hirannor.hexadocs.adapter.chunking.npl;

import com.ibm.icu.text.BreakIterator;
import io.github.hirannor.hexadocs.application.document.port.Chunk;
import io.github.hirannor.hexadocs.application.document.port.ChunkText;
import io.github.hirannor.hexadocs.application.document.port.TextChunker;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Component
class NplTextChunker implements TextChunker {

    private static final int MAX_TOKENS = 180;
    private static final int OVERLAP_SENTENCES = 1;

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

            int tokens = 0;
            int end = start;

            final List<String> buffer = new ArrayList<>();

            while (end < sentences.size()) {

                final String sentence = sentences.get(end);
                final int sentenceTokens = estimateTokens(sentence);

                if (tokens + sentenceTokens > MAX_TOKENS && !buffer.isEmpty()) {
                    break;
                }

                buffer.add(sentence);
                tokens += sentenceTokens;
                end++;
            }

            final String content = String.join(" ", buffer).trim();

            result.add(
                    Chunk.of(command.document(), content, order++)
            );

            start = Math.max(start + 1, end - OVERLAP_SENTENCES);
        }

        return result;
    }

    private List<String> extractSentences(
            final String text,
            final DocumentLanguage language
    ) {
        final Locale locale = Optional.ofNullable(language)
                .map(mapLocale)
                .orElse(Locale.ROOT);

        final BreakIterator iterator = BreakIterator.getSentenceInstance(locale);
        iterator.setText(text);

        final List<String> sentences = new ArrayList<>();

        int start = iterator.first();

        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {

            final String sentence = text.substring(start, end).trim();

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