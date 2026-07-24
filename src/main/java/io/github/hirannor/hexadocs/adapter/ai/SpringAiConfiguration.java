package io.github.hirannor.hexadocs.adapter.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableConfigurationProperties(SpringAiConfigurationProperties.class)
@ConditionalOnProperty(prefix = "adapter.ai",
        name = "provider",
        havingValue = "spring-ai")
public class SpringAiConfiguration {

    @Bean
    ChatClient chatClient(final ChatClient.Builder builder, final SpringAiConfigurationProperties properties) {
        return builder.defaultOptions(
                ChatOptions.builder().model(properties.chat().model()).temperature(properties.chat().temperature())
                        .maxTokens(properties.chat().maxTokens()).build()).build();
    }

    @Bean
    EmbeddingModel embeddingModel(final OllamaApi ollamaApi, final SpringAiConfigurationProperties properties) {
        return OllamaEmbeddingModel.builder().ollamaApi(ollamaApi)
                .defaultOptions(OllamaOptions.builder().model(properties.embedding().model()).build()).build();
    }
}
