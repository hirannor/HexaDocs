package io.github.hirannor.hexadocs.application.chat.event;

import io.github.hirannor.hexadocs.application.chat.port.AnswerPublisher;
import io.github.hirannor.hexadocs.application.chat.service.AnswerGenerated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnswerEventHandler {

    private final AnswerPublisher answer;

    public AnswerEventHandler(final AnswerPublisher answer) {
        this.answer = answer;
    }

    @EventListener
    void handle(final AnswerGenerated event) {
        answer.publish(event.answer());
    }
}
