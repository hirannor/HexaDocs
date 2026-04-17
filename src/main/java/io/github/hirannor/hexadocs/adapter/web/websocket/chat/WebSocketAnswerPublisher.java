package io.github.hirannor.hexadocs.adapter.web.websocket.chat;

import io.github.hirannor.hexadocs.application.chat.port.AnswerPublisher;
import io.github.hirannor.hexadocs.application.chat.usecase.Answer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
class WebSocketAnswerPublisher implements AnswerPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    WebSocketAnswerPublisher(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publish(final Answer answer) {
        messagingTemplate.convertAndSend(
                "/topic/chat",
                new ChatAnswerMessage(answer.content())
        );
    }
}