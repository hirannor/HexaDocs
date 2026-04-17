package io.github.hirannor.hexadocs.domain.document;

import io.github.hirannor.hexadocs.domain.document.events.DocumentRegistered;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;

import java.util.Objects;

public class Document extends AggregateRoot {
    private final DocumentId id;
    private final KnowledgeBaseId knowledgeBaseId;
    private final FileReference fileReference;
    private final DocumentLanguage language;

    private String name;

    public Document(final DocumentId id,
                    final KnowledgeBaseId kbId,
                    final String name,
                    final FileReference fileReference,
                    final DocumentLanguage language) {
        this.id = Objects.requireNonNull(id);
        this.knowledgeBaseId = Objects.requireNonNull(kbId);
        this.name = Objects.requireNonNull(name);
        this.fileReference = fileReference;
        this.language = language;
    }

    public static DocumentBuilder empty() {
        return new DocumentBuilder();
    }

    public static Document register(final DocumentId documentId,
                                    final KnowledgeBaseId kbId,
                                    final String name,
                                    final FileReference fileReference,
                                    final DocumentLanguage language) {
        final Document document = new DocumentBuilder()
                .id(documentId)
                .kbId(kbId)
                .name(name)
                .fileReference(fileReference)
                .language(language)
                .createDocument();

        document.addEvent(DocumentRegistered.record(document.id, document.knowledgeBaseId, document.name));

        return document;
    }

    public DocumentId id() {
        return id;
    }

    public KnowledgeBaseId knowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String name() {
        return name;
    }

    public FileReference fileReference() {
        return fileReference;
    }

    public DocumentLanguage language() {
        return language;
    }
}
