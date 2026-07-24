package io.github.hirannor.hexadocs.application.document.port.storage.knowledge;

import io.github.hirannor.hexadocs.domain.document.DocumentId;
import io.github.hirannor.hexadocs.domain.knowledgebase.KnowledgeBaseId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record VectorDocument(String id, String chunkId, String chunkHash, int chunkOrder, DocumentId documentId,
                             KnowledgeBaseId knowledgeBaseId, String content, Map<String, Object> metadata) {

    public static Builder empty() {
        return new Builder();
    }

    public static final class Builder {

        private final Map<String, Object> metadata = new HashMap<>();
        private String id = UUID.randomUUID().toString();
        private String chunkId;
        private String chunkHash;
        private Integer chunkOrder;
        private DocumentId documentId;
        private KnowledgeBaseId knowledgeBaseId;
        private String content;

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder chunkId(final String chunkId) {
            this.chunkId = chunkId;

            metadata.put("chunkId", chunkId);

            return this;
        }

        public Builder chunkHash(final String chunkHash) {
            this.chunkHash = chunkHash;

            metadata.put("chunkHash", chunkHash);

            return this;
        }

        public Builder chunkOrder(final int chunkOrder) {
            this.chunkOrder = chunkOrder;

            metadata.put("chunkOrder", chunkOrder);

            return this;
        }

        public Builder documentId(final DocumentId documentId) {
            this.documentId = documentId;

            if (documentId != null) {
                metadata.put("documentId", documentId.asText());
            }

            return this;
        }

        public Builder knowledgeBaseId(final KnowledgeBaseId knowledgeBaseId) {
            this.knowledgeBaseId = knowledgeBaseId;

            if (knowledgeBaseId != null) {
                metadata.put("knowledgeBaseId", knowledgeBaseId.asText());
            }

            return this;
        }

        public Builder content(final String content) {
            this.content = content;
            return this;
        }

        public Builder metadata(final String key, final Object value) {
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Metadata key cannot be blank");
            }

            if (value != null) {
                metadata.put(key, value);
            }

            return this;
        }

        public Builder metadata(final Map<String, Object> metadata) {
            if (metadata != null) {
                this.metadata.putAll(metadata);
            }

            return this;
        }

        public VectorDocument assemble() {

            if (id == null || id.isBlank()) {
                throw new IllegalStateException("id is required");
            }

            if (chunkId == null || chunkId.isBlank()) {
                throw new IllegalStateException("chunkId is required");
            }

            if (chunkHash == null || chunkHash.isBlank()) {
                throw new IllegalStateException("chunkHash is required");
            }

            if (chunkOrder == null || chunkOrder < 0) {
                throw new IllegalStateException("chunkOrder is required");
            }

            if (documentId == null) {
                throw new IllegalStateException("documentId is required");
            }

            if (knowledgeBaseId == null) {
                throw new IllegalStateException("knowledgeBaseId is required");
            }

            if (content == null || content.isBlank()) {
                throw new IllegalStateException("content is required");
            }

            return new VectorDocument(id, chunkId, chunkHash, chunkOrder, documentId, knowledgeBaseId, content,
                    Map.copyOf(metadata));
        }
    }
}
