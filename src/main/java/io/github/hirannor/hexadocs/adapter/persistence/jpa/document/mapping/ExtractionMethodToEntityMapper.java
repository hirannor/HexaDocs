package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.ExtractionMethodEntity;
import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractionMethod;

import java.util.function.Function;

public class ExtractionMethodToEntityMapper implements Function<ExtractionMethod, ExtractionMethodEntity> {
    public ExtractionMethodToEntityMapper() {
    }

    @Override
    public ExtractionMethodEntity apply(final ExtractionMethod domain) {
        if (domain == null) {
            return null;
        }

        return switch (domain) {
            case TEXT_LAYER -> ExtractionMethodEntity.TEXT_LAYER;
            case OCR -> ExtractionMethodEntity.OCR;
        };
    }
}
