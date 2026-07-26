package io.github.hirannor.hexadocs.domain.knowledgebase;

public class KnowledgeBaseNotFound extends RuntimeException {
    public KnowledgeBaseNotFound(final String message) {
        super(message);
    }
}