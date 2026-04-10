package java.io.github.hirannor.hexadocs.domain.document;

import java.io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import java.io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Document extends AggregateRoot {
    private final DocumentId id;
    private final KnowledgeBaseId knowledgeBaseId;
    private final List<Chunk> chunks;

    private DocumentStatus status;
    private String name;

    public Document(final DocumentId id, final KnowledgeBaseId kbId, final String name) {
        this.id = Objects.requireNonNull(id);
        this.knowledgeBaseId = Objects.requireNonNull(kbId);
        this.name = Objects.requireNonNull(name);
        this.status = DocumentStatus.UPLOADED;
        this.chunks = new ArrayList<>();
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

    public DocumentStatus status() {
        return status;
    }

    public List<Chunk> chunks() {
        return Collections.unmodifiableList(chunks);
    }

    public void rename(final String newName) {
        this.name = Objects.requireNonNull(newName);
    }

    public void markProcessing() {
        this.status = DocumentStatus.PROCESSING;
    }

    public void markProcessed() {
        this.status = DocumentStatus.PROCESSED;
    }

    public void markFailed() {
        this.status = DocumentStatus.FAILED;
    }

    public void addChunk(final Chunk chunk) {
        this.chunks.add(Objects.requireNonNull(chunk));
    }

}
