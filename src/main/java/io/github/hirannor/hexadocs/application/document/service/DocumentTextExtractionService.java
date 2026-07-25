package io.github.hirannor.hexadocs.application.document.service;

import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractedPage;
import io.github.hirannor.hexadocs.application.document.port.extraction.TextExtractor;
import io.github.hirannor.hexadocs.application.document.port.storage.document.DocumentStorage;
import io.github.hirannor.hexadocs.application.document.port.storage.documenttext.DocumentTextStorage;
import io.github.hirannor.hexadocs.application.document.usecase.DocumentTextExtracting;
import io.github.hirannor.hexadocs.application.document.usecase.ExtractDocumentText;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DocumentTextExtractionService implements DocumentTextExtracting {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTextExtractionService.class);

    private final DocumentStorage storage;
    private final TextExtractor extractor;
    private final DocumentTextStorage documentTextStorage;
    private final MessagePublisher messages;

    DocumentTextExtractionService(final DocumentStorage storage, final TextExtractor extractor,
                                  final MessagePublisher messages, final DocumentTextStorage documentTextStorage) {
        this.storage = storage;
        this.extractor = extractor;
        this.messages = messages;
        this.documentTextStorage = documentTextStorage;
    }

    @Override
    public void extract(final ExtractDocumentText command) {
        try {
            LOGGER.info(
                    "Starting document text extraction | " + "documentId={} | ingestionJobId={} | " + "knowledgeBaseId={}",
                    command.documentId(), command.ingestionJobId(), command.knowledgeBaseId());

            final byte[] file = storage.loadById(command.documentId())
                    .orElseThrow(() -> new IllegalStateException("Document not found: " + command.documentId()));

            LOGGER.info("Document loaded from storage | " + "documentId={} | sizeBytes={}", command.documentId(),
                    file.length);

            LOGGER.info("Extracting text from document | documentId={}", command.documentId());

            final List<ExtractedPage> pages = extractor.extract(file);

            LOGGER.info("Text extraction completed | documentId={} | " + "pageCount={}", command.documentId(),
                    pages.size());

            if (pages.isEmpty()) {
                LOGGER.info("No pages extracted | documentId={}", command.documentId());

                throw new IllegalStateException("No pages extracted");
            }

            final int totalCharacters = pages.stream().mapToInt(page -> page.text().length()).sum();

            if (totalCharacters == 0) {
                LOGGER.info("Extracted text is empty | documentId={}", command.documentId());

                throw new IllegalStateException("Empty extracted text");
            }

            LOGGER.info("Saving extracted pages | documentId={} | " + "pageCount={} | totalCharacters={}",
                    command.documentId(), pages.size(), totalCharacters);

            documentTextStorage.save(command.documentId(), pages);

            LOGGER.info("Publishing DocumentTextExtracted event | " + "documentId={} | ingestionJobId={}",
                    command.documentId(), command.ingestionJobId());

            messages.publish(DocumentTextExtracted.record(command.ingestionJobId(), command.documentId(),
                    command.knowledgeBaseId()));

            LOGGER.info("Document text extraction completed successfully | " + "documentId={}", command.documentId());

        } catch (final Exception e) {
            LOGGER.error("Document text extraction failed | documentId={}", command.documentId(), e);

            messages.publish(DocumentTextExtractionFailed.record(command.ingestionJobId(), e.getMessage()));
        }
    }
}
