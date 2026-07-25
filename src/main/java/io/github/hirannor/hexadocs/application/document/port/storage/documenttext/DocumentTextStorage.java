package io.github.hirannor.hexadocs.application.document.port.storage.documenttext;

import io.github.hirannor.hexadocs.application.document.port.extraction.ExtractedPage;
import io.github.hirannor.hexadocs.domain.document.DocumentId;

import java.util.List;


/**
 * Provides persistent storage for extracted document text.
 */
public interface DocumentTextStorage {

    /**
     * Stores the extracted pages of a document.
     *
     * @param id    the identifier of the document
     * @param pages the extracted pages to store
     */
    void save(DocumentId id, List<ExtractedPage> pages);

    /**
     * Loads the extracted pages of a document.
     *
     * @param id the identifier of the document
     * @return the extracted pages
     */
    List<ExtractedPage> load(DocumentId id);
}
