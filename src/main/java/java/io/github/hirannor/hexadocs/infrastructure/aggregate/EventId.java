package java.io.github.hirannor.hexadocs.infrastructure.aggregate;

import java.util.UUID;

public record EventId(UUID uniqueComponent) {
    public EventId {
        if (uniqueComponent == null)
            throw new IllegalArgumentException("EventId can't be null");
    }

    public static EventId from(final String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                "EventId can't be null or empty"
            );
        }

        return new EventId(UUID.fromString(source));
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID());
    }

    public String asText() {
        return this.uniqueComponent.toString();
    }
}