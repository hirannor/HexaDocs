package io.github.hirannor.hexadocs.application.document.port.storage.documenttext;

import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractedPage;
import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.util.List;

public interface DocumentTextStorage {
    void save(DocumentId id, List<ExtractedPage> pages);

    List<ExtractedPage> load(DocumentId id);
}
