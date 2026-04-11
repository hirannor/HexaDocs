package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.DocumentStorage;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentUpload;
import io.github.hirannor.hexadocs.domain.document.*;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class DocumentUploadService implements DocumentUpload {
    private final DocumentRepository documents;
    private final DocumentStorage documentStorage;
    private final MessagePublisher messages;

    DocumentUploadService(final DocumentRepository documents,
                          final DocumentStorage documentStorage,
                          final MessagePublisher messages) {
        this.documents = documents;
        this.documentStorage = documentStorage;
        this.messages = messages;
    }

    @Override
    public DocumentId upload(final UploadDocument command, final byte[] content) {
        final DocumentId id = DocumentId.generate();

        documentStorage.store(
                id,
                content,
                command.contentType()
        );
        final Document document = Document.register(
                id,
                command.knowledgeBaseId(),
                command.name(),
                FileReference.of(id.asText())
        );
        documents.save(document);

        document.events()
                .forEach(messages::publish);

        return id;
    }
}
