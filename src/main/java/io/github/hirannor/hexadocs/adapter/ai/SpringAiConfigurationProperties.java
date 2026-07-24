package io.github.hirannor.hexadocs.adapter.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("adapter.ai")
public record SpringAiConfigurationProperties(String provider, ChatProperties chat, EmbeddingProperties embedding) {

    public record ChatProperties(String model, double temperature, int maxTokens, RetrievalProperties retrieval) {
    }

    public record RetrievalProperties(int topK, double similarityThreshold) {
    }

    public record EmbeddingProperties(String model) {
    }
}
