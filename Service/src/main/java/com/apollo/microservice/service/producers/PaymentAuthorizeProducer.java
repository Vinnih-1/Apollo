package com.apollo.microservice.service.producers;

import com.apollo.microservice.service.models.AuthorizeModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentAuthorizeProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishPaymentAuthorizeResponse(AuthorizeModel authorizeModel) {
        rabbitTemplate.convertAndSend("", "authorizer.discord", authorizeModel);
    }
}