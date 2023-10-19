package com.apollo.microservice.payment.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value(value = "${broker.queue.payment.name}")
    private String queue;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }
}