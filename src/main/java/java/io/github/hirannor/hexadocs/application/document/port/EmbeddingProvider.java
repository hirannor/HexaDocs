package java.io.github.hirannor.hexadocs.application.document.port;

import java.io.github.hirannor.hexadocs.domain.document.Embedding;
import java.util.List;

public interface EmbeddingProvider {
    List<Embedding> embed(final List<String> texts);
}
