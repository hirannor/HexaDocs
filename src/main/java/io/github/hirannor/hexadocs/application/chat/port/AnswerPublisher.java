package io.github.hirannor.hexadocs.application.chat.port;

import io.github.hirannor.hexadocs.application.chat.usecase.Answer;

public interface AnswerPublisher {
    void publish(final Answer answer);
}