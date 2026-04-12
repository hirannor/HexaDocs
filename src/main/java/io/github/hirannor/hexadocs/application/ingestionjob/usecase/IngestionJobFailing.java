package io.github.hirannor.hexadocs.application.ingestionjob.usecase;

public interface IngestionJobFailing {
    void fail(final FailIngestionJob command);
}