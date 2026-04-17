package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import io.github.hirannor.hexadocs.application.document.port.TextExtractor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class SimplePdfTextExtractor implements TextExtractor {

    @Override
    public String extract(final byte[] file) {
        if (file == null || file.length == 0) {
            throw new IllegalArgumentException("PDF file is empty");
        }

        try (final PDDocument document = Loader.loadPDF(file)) {

            if (document.isEncrypted()) {
                throw new IllegalStateException("Encrypted PDFs are not supported");
            }

            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);

            return stripper.getText(document);

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF", e);
        }
    }
}