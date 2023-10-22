package com.microservice.discord.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConfig {

    @Value(value = "${broker.queue.discord.name}")
    private String queue;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }
}