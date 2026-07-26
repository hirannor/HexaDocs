package io.github.hirannor.hexadocs.adapter.web.rest.chat;

public class ChatRequest {

    private String knowledgeBaseId;

    private String conversationId;

    private String question;

    public ChatRequest() {
    }


    public ChatRequest(final String knowledgeBaseId, final String conversationId, final String question) {
        this.knowledgeBaseId = knowledgeBaseId;
        this.conversationId = conversationId;
        this.question = question;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(final String knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(final String conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }
}