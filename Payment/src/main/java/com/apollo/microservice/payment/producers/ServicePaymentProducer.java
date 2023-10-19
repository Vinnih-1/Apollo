package com.apollo.microservice.payment.producers;

import com.apollo.microservice.payment.models.PaymentModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.service.name}")
    private String routingKey;

    public void sendProductPaymentMessage(PaymentModel paymentModel) {
        rabbitTemplate.convertAndSend("", routingKey, paymentModel);
    }
}