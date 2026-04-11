package io.github.hirannor.hexadocs.infrastructure.messaging;

public interface MessagePublisher {
    void publish(final Message message);
}