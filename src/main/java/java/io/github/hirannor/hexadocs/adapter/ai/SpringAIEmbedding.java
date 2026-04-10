package java.io.github.hirannor.hexadocs.adapter.ai;

import java.io.github.hirannor.hexadocs.application.document.port.EmbeddingProvider;
import java.io.github.hirannor.hexadocs.domain.document.Embedding;
import java.util.List;

class SpringAIEmbedding implements EmbeddingProvider {

    SpringAIEmbedding() {}

    @Override
    public List<Embedding> embed(final List<String> texts) {
        return List.of();
    }
}
