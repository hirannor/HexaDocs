package io.github.hirannor.hexadocs.adapter.web.rest.conversation;

public class CreateConversationRequest {

    private String title;

    public CreateConversationRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}