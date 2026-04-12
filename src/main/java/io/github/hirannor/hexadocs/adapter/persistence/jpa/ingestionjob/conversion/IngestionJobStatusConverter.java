package io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.conversion;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.ingestionjob.JobStatusEntity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IngestionJobStatusConverter
        implements AttributeConverter<JobStatusEntity, String> {

    IngestionJobStatusConverter() {
    }

    @Override
    public String convertToDatabaseColumn(
            final JobStatusEntity statusAttribute) {
        if (statusAttribute == null) return null;

        return statusAttribute.dbRepresentation();
    }

    @Override
    public JobStatusEntity convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;

        return JobStatusEntity.from(dbData);
    }

}