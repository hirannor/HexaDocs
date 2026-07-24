package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "HEX_DOCUMENT_TEXTS",
        uniqueConstraints = @UniqueConstraint(name = "UK_DOCUMENT_TEXT_DOCUMENT_PAGE",
                columnNames = {"DOCUMENT_ID", "PAGE_NUMBER"}))
public class DocumentTextEntity {
    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "document_text_seq")
    @SequenceGenerator(name = "document_text_seq",
            sequenceName = "document_text_seq",
            allocationSize = ALLOCATION_SIZE)
    private Long id;

    @Column(name = "DOCUMENT_ID",
            nullable = false)
    private String documentId;

    @Column(name = "PAGE_NUMBER",
            nullable = false)
    private int pageNumber;

    @Lob
    @Column(name = "CONTENT",
            nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "CREATED_AT",
            nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "EXTRACTION_METHOD",
            nullable = false)
    private ExtractionMethodEntity extractionMethod;

    protected DocumentTextEntity() {
    }

    public Long id() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ExtractionMethodEntity getExtractionMethod() {
        return extractionMethod;
    }

    public void setExtractionMethod(ExtractionMethodEntity extractionMethod) {
        this.extractionMethod = extractionMethod;
    }
}
