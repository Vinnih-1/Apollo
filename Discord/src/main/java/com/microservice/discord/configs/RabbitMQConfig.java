package com.microservice.discord.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConfig {

    @Bean
    public Queue queue() {
        return new Queue("payment.discord", true);
    }
}