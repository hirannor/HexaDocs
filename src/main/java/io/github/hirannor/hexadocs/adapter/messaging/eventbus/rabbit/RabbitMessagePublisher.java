package io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit;

import io.github.hirannor.hexadocs.infrastructure.messaging.Message;
import io.github.hirannor.hexadocs.infrastructure.messaging.MessagePublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class RabbitMessagePublisher implements MessagePublisher {
    private static final Logger LOGGER = LogManager.getLogger(RabbitMessagePublisher.class);
    private static final String ROUTING_KEY = "hexa.events";

    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfigurationProperties properties;

    RabbitMessagePublisher(final RabbitTemplate rabbitTemplate,
                           final RabbitConfigurationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @Override
    public void publish(final Message message) {

    }
}
