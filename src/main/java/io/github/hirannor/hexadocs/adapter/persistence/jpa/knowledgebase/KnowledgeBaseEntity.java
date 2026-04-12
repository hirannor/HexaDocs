package io.github.hirannor.hexadocs.adapter.persistence.jpa.knowledgebase;

import jakarta.persistence.*;

@Entity
@Table(name = "HEX_KNOWLEDGE_BASES")
public class KnowledgeBaseEntity {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kb_seq"
    )
    @SequenceGenerator(
            name = "kb_seq",
            sequenceName = "kb_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "KNOWLEDGE_BASE_ID", nullable = false, unique = true)
    private String knowledgeBaseId;

    @Column(name = "NAME", nullable = false)
    private String name;

    protected KnowledgeBaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String getName() {
        return name;
    }

}