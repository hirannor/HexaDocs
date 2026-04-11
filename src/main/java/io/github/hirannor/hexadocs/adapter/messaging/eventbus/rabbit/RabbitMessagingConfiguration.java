package io.github.hirannor.hexadocs.adapter.messaging.eventbus.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Objects;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.messaging",
        havingValue = "rabbitmq"
)
@EnableScheduling
@EnableConfigurationProperties(RabbitConfigurationProperties.class)
public class RabbitMessagingConfiguration {

    private final RabbitConfigurationProperties properties;
    private final ObjectMapper objectMapper;
    private final RabbitProperties rabbitProperties;

    @Autowired
    RabbitMessagingConfiguration(final RabbitConfigurationProperties properties,
                                 final ObjectMapper objectMapper,
                                 final RabbitProperties rabbitProperties) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    TopicExchange hexaExchange() {
        return new TopicExchange(properties.getExchange());
    }


    @Bean
    Queue hexaQueue() {
        return QueueBuilder.durable(properties.getQueue())
                .build();
    }

    @Bean
    Binding createHexaQueueBinding(@Qualifier("hexaQueue") final Queue omsQueue,
                                   @Qualifier("hexaExchange") final TopicExchange omsExchange) {
        return BindingBuilder.bind(omsQueue).to(omsExchange).with("hexa.events");
    }

    @Bean
    ConnectionFactory connectionFactory() {
        final CachingConnectionFactory connection = new CachingConnectionFactory();
        connection.setHost(rabbitProperties.getHost());
        connection.setPort(rabbitProperties.getPort());
        connection.setUsername(rabbitProperties.getUsername());
        connection.setPassword(rabbitProperties.getPassword());
        return connection;
    }

    @Bean(name = "rabbitListenerContainerFactory")
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            final SimpleRabbitListenerContainerFactoryConfigurer configurer) {

        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, this.connectionFactory());
        Objects.requireNonNull(factory);

        factory.setMessageConverter(createJacksonMessageConverter());

        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(properties.getRetry().getMaxAttempts())
                        .backOffOptions(2000, 2.0, 10000)
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .build()
        );

        return factory;
    }


    @Bean
    RabbitTemplate rabbitTemplate() {
        final RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(createJacksonMessageConverter());
        return template;
    }

    private Jackson2JsonMessageConverter createJacksonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}