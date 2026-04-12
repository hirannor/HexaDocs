package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import jakarta.persistence.*;

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

    protected DocumentTextEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getContent() {
        return content;
    }
}