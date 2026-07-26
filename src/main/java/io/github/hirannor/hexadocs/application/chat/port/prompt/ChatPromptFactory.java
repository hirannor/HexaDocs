package io.github.hirannor.hexadocs.application.chat.port.prompt;

import java.util.List;

/**
 * Creates prompts used by the chat application.
 */
public interface ChatPromptFactory {

    /**
     * Creates a prompt for answering a specific question using retrieved document context.
     *
     * @param question the user's question
     * @param history  previous conversation history
     * @param context  relevant document context
     * @return the formatted prompt
     */
    String specificQuestion(String question, String history, String context);

    /**
     * Creates a prompt for analyzing a section of a document.
     *
     * @param question the user's question
     * @param context  the document section to analyze
     * @return the formatted prompt
     */
    String documentAnalysis(
            String question,
            String context
    );

    /**
     * Creates a prompt for synthesizing a final answer from document analyses.
     *
     * @param question the user's question
     * @param history  previous conversation history
     * @param analyses the analyses of the document sections
     * @return the formatted prompt
     */
    String documentWide(String question, String history, List<String> analyses);

    /**
     * Returns the system prompt.
     *
     * @return the system prompt
     */
    String system();
}