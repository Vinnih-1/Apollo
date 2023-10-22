package com.microservice.discord.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeServiceConsumer {

    @RabbitListener(queues =  "${broker.queue.service.name}")
    public void listenAuthorizeServiceQueue(@Payload Object object) {
        System.out.println(object.toString());

    }
}