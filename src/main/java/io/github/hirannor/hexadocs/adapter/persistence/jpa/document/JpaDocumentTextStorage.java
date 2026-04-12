package io.github.hirannor.hexadocs.adapter.persistence.jpa.document;

import io.github.hirannor.hexadocs.application.document.port.DocumentTextStorage;
import io.github.hirannor.hexadocs.domain.document.DocumentId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class JpaDocumentTextStorage implements DocumentTextStorage {
    JpaDocumentTextStorage() {
    }

    @Override
    public void save(final DocumentId id, final String text) {

    }

    @Override
    public Optional<String> load(final DocumentId id) {
        return Optional.empty();
    }
}
