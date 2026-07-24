package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractedPage;
import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractionMethod;
import io.github.hirannor.hexadocs.application.document.port.extraction.TextExtractor;
import net.sourceforge.tess4j.ITesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Component
public class HybridPdfTextExtractor implements TextExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HybridPdfTextExtractor.class);

    private static final int OCR_DPI = 300;

    private final ITesseract tesseract;

    public HybridPdfTextExtractor(final ITesseract tesseract) {
        this.tesseract = tesseract;
    }

    @Override
    public List<ExtractedPage> extract(final byte[] file) {
        if (file == null || file.length == 0) {
            throw new IllegalArgumentException("PDF is empty");
        }

        try (final PDDocument document = Loader.loadPDF(file)) {
            if (document.isEncrypted()) {
                throw new IllegalStateException("Encrypted PDFs are not supported");
            }

            return extractPages(document);

        } catch (final Exception e) {
            throw new IllegalStateException("PDF extraction failed", e);
        }
    }

    private List<ExtractedPage> extractPages(final PDDocument document) throws Exception {
        final PDFRenderer renderer = new PDFRenderer(document);

        final List<ExtractedPage> pages = new ArrayList<>(document.getNumberOfPages());

        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
            final int pageNumber = pageIndex + 1;

            final String extractedText = extractText(document, pageIndex);

            if (isUsableText(extractedText)) {
                final String cleanedText = clean(extractedText);

                LOGGER.debug("PDF text layer extracted | page={} | characters={}", pageNumber, cleanedText.length());

                LOGGER.trace("Extracted page text | page={} | text={}", pageNumber, cleanedText);

                pages.add(new ExtractedPage(pageNumber, cleanedText, ExtractionMethod.TEXT_LAYER));

                continue;
            }

            LOGGER.debug("PDF text layer unusable, falling back to OCR | page={}", pageNumber);

            final BufferedImage image = renderer.renderImageWithDPI(pageIndex, OCR_DPI, ImageType.RGB);
            final String ocrText = tesseract.doOCR(image);
            final String cleanedText = clean(ocrText);

            LOGGER.debug("OCR completed | page={} | characters={}", pageNumber, cleanedText.length());
            LOGGER.trace("Extracted OCR text | page={} | text={}", pageNumber, cleanedText);

            pages.add(new ExtractedPage(pageNumber, cleanedText, ExtractionMethod.OCR));
        }

        return pages;
    }

    private String extractText(final PDDocument document, final int pageIndex) throws Exception {
        final PDFTextStripper stripper = new PDFTextStripper();

        stripper.setSortByPosition(true);
        stripper.setShouldSeparateByBeads(true);
        stripper.setStartPage(pageIndex + 1);
        stripper.setEndPage(pageIndex + 1);

        return stripper.getText(document);
    }

    private boolean isUsableText(final String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        final long replacementCharacters = text.chars().filter(character -> character == '\uFFFD').count();
        final double replacementRatio = (double) replacementCharacters / text.length();

        if (replacementRatio > 0.02) {
            return false;
        }

        final long alphanumericCharacters = text.chars().filter(Character::isLetterOrDigit).count();

        return alphanumericCharacters >= 50;
    }

    private String clean(final String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\u0000", "").replace("\r\n", "\n").replace('\r', '\n').replaceAll("[ \\t]+", " ")
                .replaceAll("\n{3,}", "\n\n").trim();
    }
}
