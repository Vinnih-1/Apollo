package com.microservice.discord.producers;

import com.microservice.discord.dtos.AuthorizeProducerDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeServiceProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.discord.name}")
    private String routingKey;

    public void publishAuthorizeRequest(AuthorizeProducerDTO authorizeProducerDTO) {
        rabbitTemplate.convertAndSend("", routingKey, authorizeProducerDTO);
    }
}