package io.github.hirannor.hexadocs.application.document.port;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record VectorDocument(
        String id,
        DocumentId documentId,
        KnowledgeBaseId knowledgeBaseId,
        String content,
        Map<String, Object> metadata
) {

    public static Builder empty() {
        return new Builder();
    }

    public static final class Builder {

        private String id = UUID.randomUUID().toString();
        private DocumentId documentId;
        private KnowledgeBaseId knowledgeBaseId;
        private String content;
        private final Map<String, Object> metadata = new HashMap<>();

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder documentId(final DocumentId documentId) {
            this.documentId = documentId;
            metadata.put("documentId", documentId.asText());
            return this;
        }

        public Builder knowledgeBaseId(final KnowledgeBaseId knowledgeBaseId) {
            this.knowledgeBaseId = knowledgeBaseId;
            metadata.put("knowledgeBaseId", knowledgeBaseId.asText());
            return this;
        }

        public Builder content(final String content) {
            this.content = content;
            return this;
        }

        public Builder metadata(final String key, final Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public Builder metadata(final Map<String, Object> metadata) {
            this.metadata.putAll(metadata);
            return this;
        }

        public VectorDocument assemble() {
            if (documentId == null) {
                throw new IllegalStateException("documentId is required");
            }
            if (knowledgeBaseId == null) {
                throw new IllegalStateException("knowledgeBaseId is required");
            }
            if (content == null || content.isBlank()) {
                throw new IllegalStateException("content is required");
            }

            return new VectorDocument(
                    id,
                    documentId,
                    knowledgeBaseId,
                    content,
                    metadata
            );
        }
    }
}