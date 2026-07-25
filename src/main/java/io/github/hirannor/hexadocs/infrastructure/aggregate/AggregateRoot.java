package io.github.hirannor.hexadocs.infrastructure.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for domain aggregates that collect domain events.
 *
 * <p>An aggregate root maintains the domain events produced during its
 * lifecycle. These events can later be published by the application or
 * infrastructure layer.</p>
 */
public abstract class AggregateRoot implements Evented {

    private final List<DomainEvent> events;

    /**
     * Creates an aggregate root with no pending domain events.
     */
    protected AggregateRoot() {
        events = new ArrayList<>();
    }

    /**
     * Returns the domain events raised by this aggregate.
     *
     * <p>The returned list is unmodifiable. Events can only be added by the
     * aggregate itself through {@link #addEvent(DomainEvent)}.</p>
     *
     * @return an unmodifiable list of pending domain events
     */
    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Removes all pending domain events from the aggregate.
     *
     * <p>This is typically called after the events have been published or
     * otherwise handled.</p>
     */
    @Override
    public void clearEvents() {
        events.clear();
    }

    /**
     * Raises a domain event.
     *
     * @param event the domain event to add
     */
    protected void addEvent(final DomainEvent event) {
        events.add(event);
    }
}