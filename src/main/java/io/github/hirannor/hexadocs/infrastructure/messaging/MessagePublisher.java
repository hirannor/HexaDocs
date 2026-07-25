package io.github.hirannor.hexadocs.infrastructure.messaging;

/**
 * Publishes messages to an external messaging infrastructure.
 */
public interface MessagePublisher {

    /**
     * Publishes a message.
     *
     * @param message the message to publish
     */
    void publish(final Message message);
}