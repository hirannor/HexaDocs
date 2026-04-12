package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "HEX_DOCUMENT_TEXTS")
public class DocumentTextEntity {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "document_text_seq"
    )
    @SequenceGenerator(
            name = "document_text_seq",
            sequenceName = "document_text_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "DOCUMENT_ID", nullable = false, unique = true)
    private String documentId;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    protected DocumentTextEntity() {
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

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}