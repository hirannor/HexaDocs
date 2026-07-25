package io.github.hirannor.hexadocs.application.chat.port;

import io.github.hirannor.hexadocs.application.chat.usecase.Answer;

/**
 * Publishes generated answers to an external system.
 */
public interface AnswerPublisher {
    /**
     * Publishes the specified answer.
     *
     * @param answer the answer to publish
     */
    void publish(final Answer answer);
}