package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping.ExtractionMethodEntityToDomainMapper;
import io.github.hirannor.hexadocs.adapter.persistence.jpa.document.mapping.ExtractionMethodToEntityMapper;
import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractedPage;
import io.github.hirannor.hexadocs.application.document.port.storage.documenttext.DocumentTextStorage;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
@Transactional
class JpaDocumentTextStorageRepository implements DocumentTextStorage {
    private final DocumentTextSpringDataJpaRepository documentTexts;
    private final ExtractionMethodEntityToDomainMapper mapToDomain;
    private final ExtractionMethodToEntityMapper mapToEntity;

    JpaDocumentTextStorageRepository(final DocumentTextSpringDataJpaRepository documentTexts) {
        this.documentTexts = documentTexts;
        this.mapToDomain = new ExtractionMethodEntityToDomainMapper();
        this.mapToEntity = new ExtractionMethodToEntityMapper();
    }

    @Override
    public void save(final DocumentId id, final List<ExtractedPage> pages) {
        final String documentId = id.asText();

        documentTexts.deleteByDocumentId(documentId);

        final Instant createdAt = Instant.now();

        final List<DocumentTextEntity> entities = pages.stream().map(page -> {
            final DocumentTextEntity entity = new DocumentTextEntity();

            entity.setDocumentId(documentId);
            entity.setPageNumber(page.pageNumber());
            entity.setContent(page.text());
            entity.setExtractionMethod(mapToEntity.apply(page.extractionMethod()));
            entity.setCreatedAt(createdAt);

            return entity;
        }).toList();

        documentTexts.saveAll(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtractedPage> load(final DocumentId id) {
        return documentTexts.findAllByDocumentIdOrderByPageNumberAsc(id.asText()).stream()
                .map(entity -> new ExtractedPage(entity.getPageNumber(), entity.getContent(),
                        mapToDomain.apply(entity.getExtractionMethod()))).toList();
    }
}
