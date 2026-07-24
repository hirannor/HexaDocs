package io.github.hirannor.hexadocs.application.document.port.chunking;

import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractionMethod;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

import java.util.Objects;

public record ChunkText(CommandId id, String text, DocumentId document, DocumentLanguage language, int pageNumber,
                        ExtractionMethod extractionMethod) implements Command {

    public ChunkText {
        Objects.requireNonNull(id, "Command ID cannot be null");

        Objects.requireNonNull(text, "Text cannot be null");

        Objects.requireNonNull(document, "Document ID cannot be null");

        Objects.requireNonNull(language, "Document language cannot be null");

        Objects.requireNonNull(extractionMethod, "Extraction method cannot be null");

        if (text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be blank");
        }

        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than zero");
        }
    }

    public static ChunkText issue(final String text, final DocumentId document, final DocumentLanguage language,
                                  final int pageNumber, final ExtractionMethod extractionMethod) {
        return new ChunkText(CommandId.generate(), text, document, language, pageNumber, extractionMethod);
    }
}
