package io.github.hirannor.hexadocs.domain.knowledgebase;

import io.github.hirannor.hexadocs.infrastructure.aggregate.AggregateRoot;

import java.util.Objects;

public class KnowledgeBase extends AggregateRoot {
    private final KnowledgeBaseId id;

    private String name;

    public KnowledgeBase(final KnowledgeBaseId id, final String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }

    public static KnowledgeBase create(final CreateKnowledgeBase command) {
        final KnowledgeBase knowledgeBase = new KnowledgeBase(
                KnowledgeBaseId.generate(),
                command.name()
        );

        knowledgeBase.addEvent(KnowledgeBaseCreated.record(knowledgeBase.id, command.name()));

        return knowledgeBase;
    }

    public KnowledgeBaseId id() {
        return id;
    }

    public String name() {
        return name;
    }


    public void rename(final String newName) {
        this.name = Objects.requireNonNull(newName);
    }

}
