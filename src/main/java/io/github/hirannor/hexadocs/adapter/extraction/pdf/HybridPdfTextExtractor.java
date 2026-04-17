package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import io.github.hirannor.hexadocs.application.document.port.TextExtractor;
import net.sourceforge.tess4j.ITesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class HybridPdfTextExtractor implements TextExtractor {

    private final ITesseract tesseract;

    public HybridPdfTextExtractor(final TesseractFactory factory) {
        this.tesseract = factory.get();
    }

    @Override
    public String extract(byte[] file) {
        if (file == null || file.length == 0) {
            throw new IllegalArgumentException("PDF is empty");
        }

        try (final PDDocument document = Loader.loadPDF(file)) {

            if (document.isEncrypted()) {
                throw new IllegalStateException("Encrypted PDFs not supported");
            }

            final String text = extractTextLayer(document);

            if (isBadText(text)) {
                return ocr(document);
            }

            return clean(text);

        } catch (Exception e) {
            throw new RuntimeException("PDF extraction failed", e);
        }
    }

    private String extractTextLayer(final PDDocument document) throws Exception {
        final PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        return stripper.getText(document);
    }

    private boolean isBadText(final String text) {
        if (text == null || text.isBlank()) return true;

        final long bad = text.chars()
                .filter(c -> c == '\uFFFD')
                .count();

        return (double) bad / text.length() > 0.02;
    }

    private String ocr(final PDDocument document) throws Exception {
        final PDFRenderer renderer = new PDFRenderer(document);

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < document.getNumberOfPages(); i++) {
            final BufferedImage image = renderer.renderImageWithDPI(i, 300);
            sb.append(tesseract.doOCR(image)).append("\n");
        }

        return clean(sb.toString());
    }

    private String clean(final String text) {
        return text == null ? null : text.replace("\u0000", "").trim();
    }
}