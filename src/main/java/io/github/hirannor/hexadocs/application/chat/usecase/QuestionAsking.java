package io.github.hirannor.hexadocs.application.chat.usecase;

public interface QuestionAsking {
    Answer ask(final AskQuestion command);
}
