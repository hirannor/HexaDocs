package io.github.hirannor.hexadocs.infrastructure.messaging;

import java.util.UUID;

public interface Message {
    static MessageId generateId() {
        return new MessageId(UUID.randomUUID());
    }

    MessageId id();
}