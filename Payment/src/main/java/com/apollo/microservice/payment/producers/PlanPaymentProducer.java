package com.apollo.microservice.payment.producers;

import com.apollo.microservice.payment.models.PaymentModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanPaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPlanPaymentService(PaymentModel paymentModel) {
        rabbitTemplate.convertAndSend("plan", "", paymentModel);
    }
}
