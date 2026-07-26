package io.github.hirannor.hexadocs.adapter.persistence.jpa.conversation;


import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "HEX_CONVERSATIONS")
public class ConversationEntity {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "conversation_seq")
    @SequenceGenerator(name = "conversation_seq",
            sequenceName = "conversation_seq",
            allocationSize = ALLOCATION_SIZE)
    private Long id;

    @Column(name = "CONVERSATION_ID",
            nullable = false,
            unique = true)
    private String conversationId;

    @Column(name = "KNOWLEDGE_BASE_ID",
            nullable = false)
    private String knowledgeBaseId;

    @Column(name = "TITLE",
            nullable = false)
    private String title;

    @Column(name = "CREATED_AT",
            nullable = false)
    private Instant createdAt;

    public ConversationEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(final String conversationId) {
        this.conversationId = conversationId;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(final String knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }
}