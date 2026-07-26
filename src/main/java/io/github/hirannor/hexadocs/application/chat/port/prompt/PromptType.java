package io.github.hirannor.hexadocs.application.chat.port.prompt;

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
     * Replaces pronouns, relative terms, and implicit context in a user query with explicit entities.
     */
    QUERY_REWRITER_USER,

    /**
     * Instructs the language model on how to reformulate multi-turn user queries for optimal vector retrieval.
     */
    QUERY_REWRITER_SYSTEM
}