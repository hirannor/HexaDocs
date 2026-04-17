package io.github.hirannor.hexadocs.domain.document;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

import java.time.Instant;

public record UploadDocument(
        CommandId id,
        Instant registeredAt,
        KnowledgeBaseId knowledgeBaseId,
        String name,
        String contentType,
        DocumentLanguage language
) implements Command {

    public static UploadDocument issue(final KnowledgeBaseId knowledgeBaseId, final String name, final String contentType, final DocumentLanguage language) {
        return new UploadDocument(CommandId.generate(), Instant.now(), knowledgeBaseId, name, contentType, language);
    }
}