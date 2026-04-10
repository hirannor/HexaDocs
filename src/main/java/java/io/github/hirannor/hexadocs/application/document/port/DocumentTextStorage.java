package java.io.github.hirannor.hexadocs.application.document.port;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.util.Optional;

public interface DocumentTextStorage {
    void save(final DocumentId id, final String text);
    Optional<String> load(final DocumentId id);
}
