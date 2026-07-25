package io.github.hirannor.hexadocs.application.document.port.extraction;

import java.util.List;

/**
 * Extracts textual content from a document file.
 */
public interface TextExtractor {

    /**
     * Extracts text from the specified file.
     *
     * @param file the binary content of the document
     * @return the extracted pages
     */
    List<ExtractedPage> extract(final byte[] file);
}
