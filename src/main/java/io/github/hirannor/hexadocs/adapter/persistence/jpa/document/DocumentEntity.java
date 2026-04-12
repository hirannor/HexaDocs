package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "HEX_DOCUMENTS")
public class DocumentEntity {
    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "documents_seq"
    )
    @SequenceGenerator(
            name = "documents_seq",
            sequenceName = "documents_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "DOCUMENT_ID")
    private String documentId;

    @Column(name = "KNOWLEDGE_BASE_ID")
    private String knowledgeBaseId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FILE_REFERENCE")
    private String fileReference;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    public DocumentEntity() {
    }

    public Long id() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(final String knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFileReference() {
        return fileReference;
    }

    public void setFileReference(final String fileReference) {
        this.fileReference = fileReference;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
