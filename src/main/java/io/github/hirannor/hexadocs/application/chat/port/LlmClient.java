package io.github.hirannor.hexadocs.application.chat.port;


/**
 * Provides access to a Large Language Model for text generation.
 */
public interface LlmClient {


    /**
     * Generates a response using the provided system and user prompts.
     *
     * @param systemPrompt the system prompt defining the behavior and constraints
     *                     of the language model
     * @param userPrompt   the user prompt containing the request and relevant context
     * @return the generated response
     */
    String generate(final String systemPrompt, final String userPrompt);
}
