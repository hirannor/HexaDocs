package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import io.github.hirannor.hexadocs.application.document.port.DocumentTextStorage;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
class JpaDocumentTextStorageRepository implements DocumentTextStorage {

    private final DocumentTextSpringDataJpaRepository documentTexts;

    JpaDocumentTextStorageRepository(final DocumentTextSpringDataJpaRepository documentTexts) {
        this.documentTexts = documentTexts;
    }

    @Override
    public void save(final DocumentId id, final String text) {
        final String documentId = id.asText();

        final DocumentTextEntity entity = new DocumentTextEntity();
        entity.setDocumentId(documentId);
        entity.setContent(text);

        documentTexts.save(entity);
    }

    @Override
    public Optional<String> load(final DocumentId id) {
        return documentTexts
                .findByDocumentId(id.asText())
                .map(DocumentTextEntity::getContent);
    }
}