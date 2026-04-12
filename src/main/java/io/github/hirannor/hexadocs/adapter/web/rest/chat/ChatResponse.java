package io.github.hirannor.hexadocs.adapter.web.rest.chat;

public class ChatResponse {
    private String answer;

    public ChatResponse() {
    }

    public ChatResponse(final String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }
}
