package io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.hexadocs.infrastructure.messaging.Message;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class RabbitMqMessagePublisher implements MessagePublisher {
    private static final String ROUTING_KEY = "hexa.events";

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqConfigurationProperties properties;

    RabbitMqMessagePublisher(final RabbitTemplate rabbitTemplate,
                             final RabbitMqConfigurationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public void publish(final Message message) {
        this.rabbitTemplate.convertAndSend(properties.getExchange(), ROUTING_KEY, message);
    }
}
