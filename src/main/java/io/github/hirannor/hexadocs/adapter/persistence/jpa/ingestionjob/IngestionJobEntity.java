package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HEX_INGESTION_JOBS")
public class IngestionJobEntity {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ingestion_job_seq"
    )
    @SequenceGenerator(
            name = "ingestion_job_seq",
            sequenceName = "ingestion_job_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "JOB_ID", nullable = false, unique = true)
    private String ingestionJobId;

    @Column(name = "DOCUMENT_ID", nullable = false)
    private String documentId;

    @Column(name = "KNOWLEDGE_BASE_ID", nullable = false)
    private String knowledgeBaseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private JobStatusEntity status;

    @ElementCollection
    @CollectionTable(
            name = "HEX_INGESTION_JOB_ERRORS",
            joinColumns = @JoinColumn(name = "JOB_PK")
    )
    @Column(name = "ERROR")
    private List<String> errors = new ArrayList<>();

    protected IngestionJobEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getIngestionJobId() {
        return ingestionJobId;
    }


    public String getDocumentId() {
        return documentId;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(final String knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public JobStatusEntity getStatus() {
        return status;
    }


    public List<String> getErrors() {
        return errors;
    }

}