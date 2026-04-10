package java.io.github.hirannor.hexadocs.application.document.usecase;

import java.io.github.hirannor.hexadocs.domain.document.CreateDocument;
import java.io.github.hirannor.hexadocs.domain.document.DocumentId;

public interface DocumentCreation {
    DocumentId create(final CreateDocument command);
}
