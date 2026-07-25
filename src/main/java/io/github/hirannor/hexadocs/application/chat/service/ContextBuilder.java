package io.github.hirannor.hexadocs.application.chat.service;


import io.github.hirannor.hexadocs.application.document.port.storage.knowledge.ContextChunk;

import java.util.List;

/**
 * Builds LLM context from document chunks.
 */
public interface ContextBuilder {

    /**
     * Builds a context from the supplied chunks.
     *
     * @param chunks the chunks to include in the context
     * @return the formatted context
     */
    String build(final List<? extends ContextChunk> chunks);

    /**
     * Splits the supplied chunks into context windows.
     *
     * @param chunks the chunks to split into windows
     * @return the resulting context windows
     */
    List<String> buildWindows(final List<? extends ContextChunk> chunks);
}