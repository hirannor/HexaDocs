package java.io.github.hirannor.hexadocs.adapter.persistence.mongo;

import java.io.github.hirannor.hexadocs.application.document.port.DocumentTextStorage;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.util.Optional;

class MongoDocumentTextStorage implements DocumentTextStorage {
    MongoDocumentTextStorage() {}

    @Override
    public void save(final DocumentId id, final String text) {

    }

    @Override
    public Optional<String> load(final DocumentId id) {
        return Optional.empty();
    }
}
