package io.github.hirannor.hexadocs.adapter.web.websocket;

public class ChatQuestionMessage {
    private String knowledgeBaseId;
    private String question;

    public ChatQuestionMessage() {
    }

    public ChatQuestionMessage(final String knowledgeBaseId, final String question) {
        this.knowledgeBaseId = knowledgeBaseId;
        this.question = question;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public void setKnowledgeBaseId(final String knowledgeBaseId) {
        this.knowledgeBaseId = knowledgeBaseId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }
}
