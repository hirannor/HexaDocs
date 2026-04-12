package io.github.hirannor.hexadocs.adapter.web.rest.knowledgebase;

public class CreateKnowledgeBaseResponse {
    private String id;

    public CreateKnowledgeBaseResponse() {
    }

    public CreateKnowledgeBaseResponse(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
