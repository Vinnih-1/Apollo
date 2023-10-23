package com.microservice.discord.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.discord.models.AuthorizeModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeServiceConsumer {

    @RabbitListener(queues =  "${broker.queue.auth.service}")
    public void listenAuthorizeServiceQueue(@Payload AuthorizeModel authorizeModel) {
        System.out.println(authorizeModel.toString());
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}