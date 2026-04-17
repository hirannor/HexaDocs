package io.github.hirannor.hexadocs.application.chat.service;

import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import io.github.hirannor.hexadocs.infrastructure.aggregate.ApplicationEvent;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessageId;

public record AnswerGenerated(MessageId id, Answer answer) implements ApplicationEvent {

    public static AnswerGenerated record(final Answer answer) {
        return new AnswerGenerated(MessageId.generate(), answer);
    }
}
