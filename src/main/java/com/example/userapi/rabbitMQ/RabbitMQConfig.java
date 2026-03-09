package com.example.userapi.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userCreatedQueue() {
        return new Queue("userCreated.queue", true);
    }

    @Bean
    public DirectExchange userCreatedExchange() {
        return new DirectExchange("userCreated.exchange");
    }

    @Bean
    public Binding userCreatedBinding(
            Queue userCreatedQueue,
            DirectExchange userCreatedExchange
    ) {
        return BindingBuilder
                .bind(userCreatedQueue)
                .to(userCreatedExchange)
                .with("userCreated.routing.key");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
