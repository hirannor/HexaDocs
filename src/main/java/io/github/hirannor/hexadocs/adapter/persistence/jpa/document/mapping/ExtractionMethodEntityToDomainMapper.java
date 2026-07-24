package io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.ExtractionMethodEntity;
import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractionMethod;

import java.util.function.Function;

public class ExtractionMethodEntityToDomainMapper implements Function<ExtractionMethodEntity, ExtractionMethod> {
    public ExtractionMethodEntityToDomainMapper() {
    }

    @Override
    public ExtractionMethod apply(final ExtractionMethodEntity entity) {
        if (entity == null) {
            return null;
        }

        return switch (entity) {
            case TEXT_LAYER -> ExtractionMethod.TEXT_LAYER;
            case OCR -> ExtractionMethod.OCR;
        };
    }
}
