package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.LlmClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
class SpringAiLlmClient implements LlmClient {

    private final ChatClient chatClient;

    SpringAiLlmClient(final ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String generate(final String systemPrompt,
                           final String userPrompt) {

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }

}