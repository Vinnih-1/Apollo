package com.apollo.microservice.service.consumers;

import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.services.PlanService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentPlanConsumer {

    @Autowired
    private PlanService planService;

    @RabbitListener(queues = "payment.plan")
    public void listenServicePaymentQueue(@Payload PaymentModel paymentModel) {
        if (paymentModel.getPaymentStatus() != PaymentStatus.PAYED) return;
        var service = planService.createNewService(paymentModel.getPayer());
    }
}
