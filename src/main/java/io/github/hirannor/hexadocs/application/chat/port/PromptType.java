package io.github.hirannor.hexadocs.application.chat.port;

/**
 * Identifies a prompt used by the chat application.
 */
public enum PromptType {

    /**
     * Defines the general behavior and constraints of the language model.
     */
    SYSTEM,

    /**
     * Analyzes a single section of a document in relation to a question.
     */
    DOCUMENT_ANALYSIS,

    /**
     * Synthesizes a final answer from multiple document analyses.
     */
    DOCUMENT_WIDE,

    /**
     * Answers a specific question using retrieved document context.
     */
    SPECIFIC_QUESTION
}