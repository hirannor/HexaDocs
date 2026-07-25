package io.github.hirannor.hexadocs.infrastructure.aggregate;

import java.util.List;

/**
 * Represents an object that can raise and expose domain events.
 */
public interface Evented {

    /**
     * Returns the domain events raised by this object.
     *
     * @return the domain events
     */
    List<DomainEvent> events();

    /**
     * Clears all pending domain events.
     */
    void clearEvents();
}