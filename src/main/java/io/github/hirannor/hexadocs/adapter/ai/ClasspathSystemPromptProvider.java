package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.SystemPromptProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
class ClasspathSystemPromptProvider implements SystemPromptProvider {

    private static final String PROMPT_PATH = "prompts/chat-system-prompt.txt";

    private final String systemPrompt;

    ClasspathSystemPromptProvider() {
        try {
            final ClassPathResource resource = new ClassPathResource(PROMPT_PATH);

            this.systemPrompt = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load system prompt: " + PROMPT_PATH, exception);
        }
    }

    @Override
    public String get() {
        return systemPrompt;
    }
}
