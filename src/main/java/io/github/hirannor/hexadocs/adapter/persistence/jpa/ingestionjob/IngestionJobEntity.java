package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob;

import jakarta.persistence.*;

import java.time.Instant;

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

    @Column(name = "ERROR")
    private String error;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    public IngestionJobEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngestionJobId() {
        return ingestionJobId;
    }

    public void setIngestionJobId(final String ingestionJobId) {
        this.ingestionJobId = ingestionJobId;
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

    public JobStatusEntity status() {
        return status;
    }

    public void setStatus(final JobStatusEntity status) {
        this.status = status;
    }

    public String error() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}