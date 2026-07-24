package io.github.hirannor.hexadocs.application.document.port.extraction;

import java.util.List;

public interface TextExtractor {
    List<ExtractedPage> extract(final byte[] file);
}
