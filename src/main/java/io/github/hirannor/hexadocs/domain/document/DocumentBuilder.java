package io.github.hirannor.hexadocs.domain.document;

import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

public class DocumentBuilder {
    private DocumentId id;
    private KnowledgeBaseId kbId;
    private String name;
    private FileReference fileReference;
    private DocumentLanguage language;

    public DocumentBuilder id(final DocumentId id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder kbId(final KnowledgeBaseId kbId) {
        this.kbId = kbId;
        return this;
    }

    public DocumentBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public DocumentBuilder fileReference(final FileReference fileReference) {
        this.fileReference = fileReference;
        return this;
    }


    public DocumentBuilder language(final DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public Document createDocument() {
        return new Document(id, kbId, name, fileReference, language);
    }
}