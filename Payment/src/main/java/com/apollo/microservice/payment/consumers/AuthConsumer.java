package com.apollo.microservice.payment.consumers;

import com.apollo.microservice.payment.enums.AuthStatus;
import com.apollo.microservice.payment.models.AuthorizeModel;
import com.apollo.microservice.payment.services.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AuthConsumer {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.auth.result}")
    private String responseRoutingKey;

    @RabbitListener(queues = "${broker.queue.auth.payment}")
    public void listenAuthRequest(@Payload AuthorizeModel authorizeModel) {
        authorizeModel.setAuthorizeUrl(paymentService.generateAuthorizationUrl(authorizeModel));
        authorizeModel.setAuthStatus(AuthStatus.AUTH_PENDING);
        publishAuthorizationUrlResponse(authorizeModel);
    }

    public void publishAuthorizationUrlResponse(AuthorizeModel authorizeModel) {
        rabbitTemplate.convertAndSend("", responseRoutingKey, authorizeModel);
    }
}