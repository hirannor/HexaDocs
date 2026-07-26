package io.github.hirannor.hexadocs.adapter.ai;

import io.github.hirannor.hexadocs.application.chat.port.prompt.PromptProvider;
import io.github.hirannor.hexadocs.application.chat.port.prompt.PromptType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

/**
 * Loads chat prompts from the application classpath.
 */
@Component
class ClasspathPromptProvider implements PromptProvider {
    private static final Map<PromptType, String> PROMPT_PATHS = createPromptPaths();

    private final Map<PromptType, String> prompts;

    ClasspathPromptProvider() {
        prompts = loadPrompts();
    }

    @Override
    public String get(final PromptType promptType) {
        return prompts.get(promptType);
    }

    private Map<PromptType, String> loadPrompts() {
        final Map<PromptType, String> loadedPrompts = new EnumMap<>(PromptType.class);

        PROMPT_PATHS.forEach((promptType, path) -> loadedPrompts.put(promptType, load(path)));

        return loadedPrompts;
    }

    private String load(final String path) {
        try {
            final ClassPathResource resource = new ClassPathResource(path);

            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load prompt: " + path, exception);
        }
    }

    private static Map<PromptType, String> createPromptPaths() {
        final Map<PromptType, String> paths = new EnumMap<>(PromptType.class);

        paths.put(PromptType.SYSTEM, "prompts/chat/system.txt");
        paths.put(PromptType.DOCUMENT_ANALYSIS, "prompts/chat/document-analysis.txt");
        paths.put(PromptType.DOCUMENT_WIDE, "prompts/chat/document-wide.txt");

        paths.put(PromptType.QUERY_REWRITER_SYSTEM, "prompts/chat/query-rewriter/system.txt");
        paths.put(PromptType.QUERY_REWRITER_USER, "prompts/chat/query-rewriter/user.txt");

        return Map.copyOf(paths);
    }
}