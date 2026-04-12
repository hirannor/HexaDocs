package io.github.hirannor.hexadocs.domain.document;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public class DocumentBuilder {
    private DocumentId id;
    private KnowledgeBaseId kbId;
    private String name;
    private FileReference fileReference;

    public DocumentBuilder id(DocumentId id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder kbId(KnowledgeBaseId kbId) {
        this.kbId = kbId;
        return this;
    }

    public DocumentBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DocumentBuilder fileReference(FileReference fileReference) {
        this.fileReference = fileReference;
        return this;
    }

    public Document createDocument() {
        return new Document(id, kbId, name, fileReference);
    }
}