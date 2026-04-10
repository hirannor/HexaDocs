package java.io.github.hirannor.hexadocs.domain.document;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;
import java.util.Objects;

public class Document extends AggregateRoot {
    private final DocumentId id;
    private final KnowledgeBaseId knowledgeBaseId;

    private String name;

    public Document(final DocumentId id, final KnowledgeBaseId kbId, final String name) {
        this.id = Objects.requireNonNull(id);
        this.knowledgeBaseId = Objects.requireNonNull(kbId);
        this.name = Objects.requireNonNull(name);
    }

    public static Document create(final CreateDocument command) {
        final Document document = new Document(
            DocumentId.generate(),
            command.knowledgeBaseId(),
            command.name()
        );

        document.addEvent(DocumentCreated.record(document.id, document.knowledgeBaseId, document.name));

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

}
