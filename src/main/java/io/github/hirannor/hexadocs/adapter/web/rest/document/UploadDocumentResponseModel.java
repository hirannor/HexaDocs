package io.github.hirannor.hexadocs.adapter.web.rest.document;

public class UploadDocumentResponseModel {
    private String documentId;

    public UploadDocumentResponseModel() {
    }

    public UploadDocumentResponseModel(final String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }
}
