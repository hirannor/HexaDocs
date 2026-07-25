package io.github.hirannor.hexadocs.adapter.chunking.npl;

import com.ibm.icu.text.BreakIterator;
import io.github.hirannor.hexadocs.application.document.port.chunking.Chunk;
import io.github.hirannor.hexadocs.application.document.port.chunking.ChunkText;
import io.github.hirannor.hexadocs.application.document.port.chunking.TextChunker;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Component
class NplTextChunker implements TextChunker {
    private static final int DEFAULT_MAX_ESTIMATED_TOKENS = 250;
    private static final int DEFAULT_OVERLAP_TOKENS = 40;
    private static final double CHARACTERS_PER_TOKEN = 4.0;

    private final Function<DocumentLanguage, Locale> mapLocale;

    NplTextChunker() {
        this.mapLocale = new DocumentLanguageToLocaleMapper();
    }

    @Override
    public List<Chunk> chunk(final ChunkText command) {
        if (command.text() == null || command.text().isBlank()) {
            return List.of();
        }

        final List<String> sentences = extractSentences(command.text(), command.language());
        if (sentences.isEmpty()) {
            return List.of();
        }

        final List<Chunk> chunks = new ArrayList<>();
        int start = 0;
        int order = 0;

        while (start < sentences.size()) {
            final ChunkWindow window = createChunkWindow(sentences, start);
            final String content = String.join(" ", window.sentences()).trim();

            if (!content.isBlank()) {
                chunks.add(Chunk.of(command.document(), command.pageNumber(), content, order++,
                        command.extractionMethod()));
            }

            if (window.end() >= sentences.size()) {
                break;
            }

            final int nextStart = calculateTokenBasedNextStart(sentences, start, window.end());
            if (nextStart <= start) {
                start = start + 1;
            } else {
                start = nextStart;
            }
        }

        return chunks;
    }

    private ChunkWindow createChunkWindow(final List<String> sentences, final int start) {
        final List<String> buffer = new ArrayList<>();
        int tokens = 0;
        int end = start;

        while (end < sentences.size()) {
            final String sentence = sentences.get(end);
            final int sentenceTokens = estimateTokens(sentence);

            if (tokens + sentenceTokens > DEFAULT_MAX_ESTIMATED_TOKENS) {
                if (buffer.isEmpty()) {
                    List<String> subParts = splitOversizedSentence(sentence);
                    buffer.addAll(subParts);
                    end++;
                }
                break;
            }

            buffer.add(sentence);
            tokens += sentenceTokens;
            end++;
        }

        return new ChunkWindow(buffer, end);
    }

    private int calculateTokenBasedNextStart(final List<String> sentences, final int currentStart,
                                             final int windowEnd) {
        int accumulatedTokens = 0;
        int newStart = windowEnd;

        while (newStart > currentStart + 1) {
            int sentenceTokens = estimateTokens(sentences.get(newStart - 1));
            if (accumulatedTokens + sentenceTokens > DEFAULT_OVERLAP_TOKENS) {
                break;
            }
            accumulatedTokens += sentenceTokens;
            newStart--;
        }

        return newStart;
    }

    private List<String> extractSentences(final String text, final DocumentLanguage language) {
        final Locale locale = Optional.ofNullable(language).map(mapLocale).orElse(Locale.ROOT);
        final BreakIterator iterator = BreakIterator.getSentenceInstance(locale);

        final String normalizedText = text.replaceAll("(\\r?\\n){2,}", ". \n");
        iterator.setText(normalizedText);

        final List<String> sentences = new ArrayList<>();
        int start = iterator.first();

        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            final String sentence = normalizedText.substring(start, end).trim();
            if (!sentence.isBlank()) {
                sentences.add(sentence);
            }
        }

        return sentences;
    }

    private List<String> splitOversizedSentence(final String sentence) {
        final List<String> chunks = new ArrayList<>();
        final StringBuilder current = new StringBuilder();

        for (final String word : sentence.split("\\s+")) {
            final String candidate = current.isEmpty() ? word : current + " " + word;

            if (estimateTokens(candidate) > DEFAULT_MAX_ESTIMATED_TOKENS && !current.isEmpty()) {
                chunks.add(current.toString());
                current.setLength(0);
                current.append(word);
            } else {
                current.setLength(0);
                current.append(candidate);
            }
        }

        if (!current.isEmpty()) {
            chunks.add(current.toString());
        }

        return chunks;
    }

    private int estimateTokens(final String text) {
        return Math.max(1, (int) Math.ceil(text.length() / CHARACTERS_PER_TOKEN));
    }

    private record ChunkWindow(List<String> sentences, int end) {
    }
}
