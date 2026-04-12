package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

import io.github.hirannor.hexadocs.domain.ingestionjob.IngestionJobId;
import io.github.hirannor.hexadocs.infrastructure.aggregate.Command;
import io.github.hirannor.hexadocs.infrastructure.aggregate.CommandId;

public record FailIngestionJob(CommandId id, IngestionJobId ingestionJobId, String error) implements Command {
    public static FailIngestionJob issue(final IngestionJobId ingestionJobId, final String error) {
        return new FailIngestionJob(CommandId.generate(), ingestionJobId, error);
    }
}
