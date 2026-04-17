package io.github.hirannor.hexadocs.adapter.web.rest.document;

import io.github.hirannor.hexadocs.application.document.usecase.DocumentUploading;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.document.DocumentLanguage;
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
import java.util.function.Function;

@RestController
@RequestMapping("/api/documents")
class DocumentUploadController {

    private final DocumentUploading document;
    private final Function<DocumentLanguageModel, DocumentLanguage> mapLanguageModelToDomain;

    DocumentUploadController(final DocumentUploading document) {
        this.document = document;
        this.mapLanguageModelToDomain = new DocumentLanguageModelToDomainMapper();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadDocumentResponseModel> upload(
            @RequestParam("file") final MultipartFile file,
            @RequestParam("name") final String name,
            @RequestParam("knowledgeBaseId") final String knowledgeBaseId,
            @RequestParam("language") final String language
    ) throws IOException {
        final DocumentLanguageModel model = DocumentLanguageModel.from(language);

        final UploadDocument command = UploadDocument.issue(
                KnowledgeBaseId.from(knowledgeBaseId),
                name,
                file.getContentType(),
                mapLanguageModelToDomain.apply(model)
        );

        final DocumentId documentId = document.upload(command, file.getBytes());

        return ResponseEntity.accepted()
                .body(new UploadDocumentResponseModel(documentId.asText()));
    }
}