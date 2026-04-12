package io.github.hirannor.hexadocs.application.document.port;

public record DocumentFile(
        String fileName,
        String contentType,
        byte[] content
) {
    public static DocumentFile of(String fileName, String contentType, byte[] content) {
        return new DocumentFile(fileName, contentType, content);
    }
}