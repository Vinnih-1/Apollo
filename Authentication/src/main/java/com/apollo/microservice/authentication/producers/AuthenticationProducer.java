package com.apollo.microservice.authentication.producers;

import com.apollo.microservice.authentication.dtos.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishAuthenticationMessage(UserDTO userDTO) {
        rabbitTemplate.convertAndSend("", "authorizer.service", userDTO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
