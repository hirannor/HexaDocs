package io.github.hirannor.hexadocs.adapter.web.rest.document;

public class UploadDocumentResponse {
    private String documentId;

    public UploadDocumentResponse() {
    }

    public UploadDocumentResponse(final String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }
}
