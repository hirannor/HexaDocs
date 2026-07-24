package io.github.hirannor.hexadocs.application.document.port.extraction;

public record ExtractedPage(int pageNumber, String text, ExtractionMethod extractionMethod) {
}
