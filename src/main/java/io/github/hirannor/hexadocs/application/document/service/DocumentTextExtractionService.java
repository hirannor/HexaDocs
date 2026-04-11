package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.DocumentStorage;
import io.github.hirannor.hexadocs.application.document.port.DocumentTextStorage;
import io.github.hirannor.hexadocs.application.document.port.TextExtractor;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentTextExtracting;
import io.github.hirannor.hexadocs.application.document.usecase.ExtractDocumentText;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.springframework.stereotype.Service;

@Service
class DocumentTextExtractionService implements DocumentTextExtracting {

    private final DocumentStorage storage;
    private final TextExtractor extractor;
    private final DocumentTextStorage documentTextStorage;
    private final MessagePublisher messages;

    DocumentTextExtractionService(
            DocumentStorage storage,
            TextExtractor extractor,
            MessagePublisher messages,
            DocumentTextStorage documentTextStorage
    ) {
        this.storage = storage;
        this.extractor = extractor;
        this.messages = messages;
        this.documentTextStorage = documentTextStorage;
    }

    @Override
    public void extract(final ExtractDocumentText command) {

        final byte[] file = storage.loadById(command.documentId());

        if (file == null || file.length == 0) {
            throw new IllegalStateException("Document not found: " + command.documentId());
        }

        final String text = extractor.extract(file);

        if (text == null || text.isBlank()) {
            throw new IllegalStateException("Empty extracted text");
        }

        documentTextStorage.save(command.documentId(), text);

        messages.publish(
                DocumentTextExtracted.record(
                        command.ingestionJobId(),
                        command.documentId(),
                        command.knowledgeBaseId()
                )
        );
    }
}