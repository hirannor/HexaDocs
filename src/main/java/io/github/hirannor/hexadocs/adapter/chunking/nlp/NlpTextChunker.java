package io.github.hirannor.hexadocs.adapter.chunking.nlp;

import com.ibm.icu.text.BreakIterator;
import io.github.hirannor.hexadocs.application.document.port.chunking.Chunk;
import io.github.hirannor.hexadocs.application.document.port.chunking.ChunkText;
import io.github.hirannor.hexadocs.application.document.port.chunking.TextChunker;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import org.springframework.ai.tokenizer.JTokkitTokenCountEstimator;
import org.springframework.ai.tokenizer.TokenCountEstimator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Component
class NlpTextChunker implements TextChunker {

    private static final int MAX_TOKENS = 250;
    private static final int OVERLAP_TOKENS = 40;
    private static final String PARAGRAPH_SEPARATOR_REGEX = "(\\r?\\n){2,}";

    private final Function<DocumentLanguage, Locale> mapLocale;

    private final TokenCountEstimator tokenCounter;

    NlpTextChunker() {
        this.mapLocale = new DocumentLanguageToLocaleMapper();
        this.tokenCounter = new JTokkitTokenCountEstimator();
    }

    @Override
    public List<Chunk> chunk(final ChunkText command) {
        if (command.text() == null || command.text()
                .isBlank()) {
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

            final String content = String.join(" ", window.sentences())
                    .trim();

            if (!content.isBlank()) {
                chunks.add(Chunk.of(command.document(), command.pageNumber(), content, order++,
                        command.extractionMethod()));
            }

            if (window.end() >= sentences.size()) {
                break;
            }

            final int nextStart = calculateNextStart(sentences, start, window.end());
            start = Math.max(start + 1, nextStart);
        }

        return chunks;
    }

    private ChunkWindow createChunkWindow(final List<String> sentences, final int start) {
        final List<String> buffer = new ArrayList<>();

        int tokenCount = 0;
        int end = start;

        while (end < sentences.size()) {

            final String sentence = sentences.get(end);
            final int sentenceTokens = countTokens(sentence);

            if (tokenCount + sentenceTokens > MAX_TOKENS) {
                if (buffer.isEmpty()) {
                    final List<String> splitSentence = splitOversizedSentence(sentence);
                    buffer.addAll(splitSentence);

                    end++;
                }
                break;
            }

            buffer.add(sentence);
            tokenCount += sentenceTokens;

            end++;
        }

        return new ChunkWindow(buffer, end);
    }

    private int calculateNextStart(final List<String> sentences, final int currentStart, final int windowEnd) {
        int overlapTokens = 0;

        int nextStart = windowEnd;

        while (nextStart > currentStart + 1) {
            final String sentence = sentences.get(nextStart - 1);

            final int sentenceTokens = countTokens(sentence);

            if (overlapTokens > 0 && overlapTokens + sentenceTokens > OVERLAP_TOKENS) {
                break;
            }

            overlapTokens += sentenceTokens;

            nextStart--;
        }

        return nextStart;
    }

    private List<String> extractSentences(final String text, final DocumentLanguage language) {
        final Locale locale = Optional.ofNullable(language)
                .map(mapLocale)
                .orElse(Locale.ROOT);

        final BreakIterator iterator = BreakIterator.getSentenceInstance(locale);

        final String normalizedText = text.replaceAll(PARAGRAPH_SEPARATOR_REGEX, ". \n");

        iterator.setText(normalizedText);

        final List<String> sentences = new ArrayList<>();

        int start = iterator.first();

        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            final String sentence = normalizedText.substring(start, end)
                    .trim();

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

            if (!current.isEmpty() && countTokens(candidate) > MAX_TOKENS) {
                chunks.add(current.toString());

                current.setLength(0);

                current.append(word);

                continue;
            }

            current.setLength(0);

            current.append(candidate);
        }

        if (!current.isEmpty()) {
            chunks.add(current.toString());
        }

        return chunks;
    }

    private int countTokens(final String text) {
        return tokenCounter.estimate(text);
    }

}
