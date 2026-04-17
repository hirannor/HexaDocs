package io.github.hirannor.hexadocs.application.document.port;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

public record ChunkText(CommandId id, String text, DocumentId document, DocumentLanguage language) implements Command {

    public static ChunkText issue(final String text, final DocumentId document, final DocumentLanguage language) {
        return new ChunkText(CommandId.generate(), text, document, language);
    }
}
