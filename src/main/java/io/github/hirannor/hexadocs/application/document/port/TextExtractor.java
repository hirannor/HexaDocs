package io.github.hirannor.hexadocs.application.document.port;

public interface TextExtractor {
    String extract(final byte[] file);
}
