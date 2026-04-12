package io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.hexadocs.infrastructure.messaging.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class RabbitMqMessageListener {

    private final ApplicationEventPublisher internalEvents;

    RabbitMqMessageListener(final ApplicationEventPublisher internalEvents) {
        this.internalEvents = internalEvents;
    }

    @RabbitListener(queues = "${messaging.rabbit.queue}")
    void onMessage(final Message msg) {
        internalEvents.publishEvent(msg);
    }
}
