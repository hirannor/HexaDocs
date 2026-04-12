package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.DocumentStorage;
import io.github.hirannor.hexadocs.application.document.port.DocumentTextStorage;
import io.github.hirannor.hexadocs.application.document.port.TextExtractor;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentTextExtracting;
import io.github.hirannor.hexadocs.application.document.usecase.ExtractDocumentText;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class DocumentTextExtractionService implements DocumentTextExtracting {

    private static final Logger log = LoggerFactory.getLogger(DocumentTextExtractionService.class);

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

        log.info("Starting document text extraction | documentId={} | ingestionJobId={} | knowledgeBaseId={}",
                command.documentId(),
                command.ingestionJobId(),
                command.knowledgeBaseId()
        );

        final byte[] file = storage.loadById(command.documentId())
                .orElseThrow(() -> new IllegalStateException(
                        "Document not found: " + command.documentId()
                ));

        log.info("Document loaded from storage | documentId={} | sizeBytes={}",
                command.documentId(),
                file != null ? file.length : 0
        );

        log.info("Extracting text from document | documentId={}", command.documentId());

        final String text = extractor.extract(file);

        log.info("Text extraction completed | documentId={} | textLength={}",
                command.documentId(),
                text != null ? text.length() : 0
        );

        if (text == null || text.isBlank()) {
            log.info("Extracted text is empty | documentId={}", command.documentId());
            throw new IllegalStateException("Empty extracted text");
        }

        log.info("Saving extracted text | documentId={}", command.documentId());

        documentTextStorage.save(command.documentId(), text);

        log.info("Publishing DocumentTextExtracted event | documentId={} | ingestionJobId={}",
                command.documentId(),
                command.ingestionJobId()
        );

        messages.publish(
                DocumentTextExtracted.record(
                        command.ingestionJobId(),
                        command.documentId(),
                        command.knowledgeBaseId()
                )
        );

        log.info("Document text extraction completed successfully | documentId={}", command.documentId());
    }
}