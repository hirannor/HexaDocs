package io.github.hirannor.hexadocs.adapter.web.rest.document;

import io.github.hirannor.hexadocs.application.document.usecase.DocumentUploading;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.UploadDocument;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
class DocumentUploadController {

    private final DocumentUploading document;

    DocumentUploadController(final DocumentUploading document) {
        this.document = document;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UploadDocumentResponse> upload(
            @RequestParam("file") final MultipartFile file,
            @RequestParam("name") final String name,
            @RequestParam("knowledgeBaseId") final String knowledgeBaseId
    ) throws IOException {
        final byte[] content = file.getBytes();

        final UploadDocument command = UploadDocument.issue(
                KnowledgeBaseId.from(knowledgeBaseId),
                name,
                file.getContentType()
        );

        final DocumentId documentId = document.upload(command, content);

        return ResponseEntity.accepted()
                .body(new UploadDocumentResponse(documentId.asText())
                );
    }
}