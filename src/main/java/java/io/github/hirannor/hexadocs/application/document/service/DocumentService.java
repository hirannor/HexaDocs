package java.io.github.hirannor.hexadocs.application.document.service;

import org.springframework.stereotype.Service;

import java.io.github.hirannor.hexadocs.application.document.usecase.DocumentCreation;
import java.io.github.hirannor.hexadocs.domain.document.CreateDocument;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.domain.document.DocumentRepository;

@Service
class DocumentService implements DocumentCreation {
    private final DocumentRepository documents;

    DocumentService(final DocumentRepository documents) {
        this.documents = documents;
    }

    @Override
    public DocumentId create(final CreateDocument command) {
        return null;
    }
}
