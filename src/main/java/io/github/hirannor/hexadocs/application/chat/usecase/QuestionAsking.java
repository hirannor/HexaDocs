package io.github.hirannor.hexadocs.application.chat.usecase;

/**
 * Provides the use case for asking questions.
 */
public interface QuestionAsking {

    /**
     * Asks a question and returns the generated answer.
     *
     * @param command the command containing the question and relevant context
     * @return the generated answer
     */
    Answer ask(final AskQuestion command);
}
