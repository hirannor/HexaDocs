package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import io.github.hirannor.hexadocs.application.document.port.TextExtractor;
import org.springframework.stereotype.Component;

@Component
class PdfTextExtractor implements TextExtractor {
    @Override
    public String extract(final byte[] file) {
        return "";
    }
}
