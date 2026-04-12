package io.github.hirannor.hexadocs.application.chat.port;

public interface LlmClient {
    String generate(final String systemPrompt, final String userPrompt);
}
