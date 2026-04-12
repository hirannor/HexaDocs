package io.github.hirannor.hexadocs.adapter.file.localfilesystem;

import io.github.hirannor.hexadocs.application.document.port.DocumentFile;
import io.github.hirannor.hexadocs.application.document.port.DocumentStorage;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

@Component
class LocalFileSystemDocumentStorage implements DocumentStorage {

    private static final String PDF_EXTENSION = ".pdf";
    private static final MimeType PDF_MIME_TYPE = MimeType.valueOf("application/pdf");

    private final String rootPath;

    LocalFileSystemDocumentStorage(final LocalFileSystemDocumentStorageConfigurationProperties config) {
        this.rootPath = config.getLocalFileSystemPath();
    }

    @Override
    public void store(final DocumentId documentId, final DocumentFile file) {
        validateDocument(file);

        try {
            final Path dir = Paths.get(rootPath).resolve(documentId.asText());
            Files.createDirectories(dir);

            final String fileName = sanitizeFileName(file.fileName()) + PDF_EXTENSION;

            final Path target = dir.resolve(fileName);

            Files.write(target, file.content(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store document " + documentId, e);
        }
    }

    @Override
    public Optional<byte[]> loadById(final DocumentId documentId) {
        try {
            final Path dir = Paths.get(rootPath).resolve(documentId.asText());

            if (!Files.exists(dir)) {
                return Optional.empty();
            }

            try (var stream = Files.list(dir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(this::isPdfExtension)
                        .findFirst()
                        .map(this::readToBytes);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load document " + documentId, e);
        }
    }

    @Override
    public void delete(final DocumentId documentId) {
        try {
            final Path dir = Paths.get(rootPath).resolve(documentId.asText());

            if (!Files.exists(dir)) {
                return;
            }

            try (final var walk = Files.walk(dir)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete document " + documentId, e);
        }
    }

    @Override
    public boolean exists(final DocumentId documentId) {
        return Files.exists(Paths.get(rootPath).resolve(documentId.asText()));
    }

    private boolean isPdfExtension(final Path p) {
        return p.toString().endsWith(PDF_EXTENSION);
    }

    private void validateDocument(final DocumentFile file) {
        if (file == null || file.content() == null || file.content().length == 0) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.contentType() == null) {
            throw new IllegalArgumentException("Content type is required");
        }

        final MimeType mimeType = MimeType.valueOf(file.contentType());

        if (!PDF_MIME_TYPE.equals(mimeType)) {
            throw new IllegalArgumentException(
                    "Only PDF files are supported. Received: " + file.contentType()
            );
        }
    }

    private byte[] readToBytes(final Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Supplier<IllegalStateException> failBecausePdfFileWasNotFoundBy(final DocumentId documentId) {
        return () -> new IllegalStateException(
                "PDF file not found for document " + documentId
        );
    }

    private String sanitizeFileName(final String name) {
        if (name == null || name.isBlank()) {
            return "document";
        }

        return name
                .replaceAll("[^a-zA-Z0-9-_]", "_")
                .toLowerCase();
    }
}