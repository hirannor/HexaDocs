package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;


import jakarta.persistence.*;

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

    @Column(name = "CUSTOMER_ID")
    private String documentId;

    @Column(name = "KNOWLEDGE_BASE_ID")
    private String knowledgeBaseId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FILE_REFERENCE")
    private String fileReference;

    protected DocumentEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String getName() {
        return name;
    }

    public String getFileReference() {
        return fileReference;
    }
}