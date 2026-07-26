package io.github.hirannor.hexadocs.application.chat.port.prompt;

/**
 * Provides prompts used by the chat application.
 */
public interface PromptProvider {

    /**
     * Returns the prompt identified by the specified prompt type.
     *
     * @param promptType the type of prompt to retrieve
     * @return the prompt content
     */
    String get(final PromptType promptType);
}