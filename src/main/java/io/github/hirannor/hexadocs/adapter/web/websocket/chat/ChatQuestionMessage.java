package io.github.hirannor.hexadocs.adapter.web.websocket.chat;

public class ChatQuestionMessage {
    private String conversationId;

    private String question;

    public ChatQuestionMessage() {
    }


    public ChatQuestionMessage(final String conversationId, final String question) {
        this.conversationId = conversationId;
        this.question = question;
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