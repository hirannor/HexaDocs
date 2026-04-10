package java.io.github.hirannor.hexadocs.application.ingestionjob.usecase;

import java.io.github.hirannor.hexadocs.domain.document.DocumentId;
import java.io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;

public interface IngestionJobStarting {
    IngestionJobId start(final DocumentId document);
}
