package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.DocumentFile;
import io.github.hirannor.hexadocs.application.document.port.DocumentStorage;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentUploading;
import io.github.hirannor.hexadocs.domain.document.*;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class DocumentUploadService implements DocumentUploading {

    private static final Logger log = LoggerFactory.getLogger(DocumentUploadService.class);

    private final DocumentMetadataRepository documentMetadata;
    private final DocumentStorage documentStorage;
    private final MessagePublisher messages;

    DocumentUploadService(final DocumentMetadataRepository documentMetadata,
                          final DocumentStorage documentStorage,
                          final MessagePublisher messages) {
        this.documentMetadata = documentMetadata;
        this.documentStorage = documentStorage;
        this.messages = messages;
    }

    @Override
    public DocumentId upload(final UploadDocument command, final byte[] content) {

        log.info("Uploading documentId | name={} | knowledgeBaseId={} | contentType={} | size={}",
                command.name(),
                command.knowledgeBaseId().asText(),
                command.contentType(),
                content != null ? content.length : 0
        );
        final DocumentId id = DocumentId.generate();

        final Document document = Document.register(
                id,
                command.knowledgeBaseId(),
                command.name(),
                FileReference.of(id.asText())
        );

        documentMetadata.save(document);

        documentStorage.store(
                id,
                DocumentFile.of(command.name(), command.contentType(), content)
        );

        document.events()
                .forEach(messages::publish);

        log.info("Document upload completed successfully | documentId={}", id.asText());

        return id;
    }
}