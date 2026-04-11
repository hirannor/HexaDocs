package io.github.hirannor.hexadocs.infrastructure.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot implements Evented {

    private final List<DomainEvent> events;

    public AggregateRoot() {
        events = new ArrayList<>();
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    protected void addEvent(final DomainEvent event) {
        events.add(event);
    }

}