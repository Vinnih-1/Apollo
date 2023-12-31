package com.apollo.microservice.payment.producers;

import com.apollo.microservice.payment.dtos.PaymentDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendProductPaymentMessage(PaymentDTO paymentDTO) {
        rabbitTemplate.convertAndSend("payment", "", paymentDTO);
    }
}