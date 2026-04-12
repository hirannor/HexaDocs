package io.github.hirannor.hexadocs.adapter.web.websocket;

public class ChatAnswerMessage {
    private String answer;

    public ChatAnswerMessage() {
    }

    public ChatAnswerMessage(final String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }
}
