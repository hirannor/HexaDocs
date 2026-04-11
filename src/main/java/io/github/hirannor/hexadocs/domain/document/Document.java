package io.github.hirannor.hexadocs.domain.document;

import io.github.hirannor.hexadocs.domain.document.events.DocumentRegistered;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;

import java.util.Objects;

public class Document extends AggregateRoot {
    private final DocumentId id;
    private final KnowledgeBaseId knowledgeBaseId;
    private final FileReference fileReference;

    private String name;

    public Document(final DocumentId id,
                    final KnowledgeBaseId kbId,
                    final String name,
                    final FileReference fileReference) {
        this.id = Objects.requireNonNull(id);
        this.knowledgeBaseId = Objects.requireNonNull(kbId);
        this.name = Objects.requireNonNull(name);
        this.fileReference = fileReference;
    }

    public static Document register(final DocumentId documentId, final KnowledgeBaseId kbId, final String name, final FileReference fileReference) {
        final Document document = new Document(
                documentId,
                kbId,
                name,
                fileReference
        );

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

}
