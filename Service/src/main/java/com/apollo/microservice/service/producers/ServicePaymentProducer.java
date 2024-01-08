package com.apollo.microservice.service.producers;

import com.apollo.microservice.service.models.PaymentModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publica o objeto de pagamento pronto para ser utilizado
     * pelo microsserviço Discord, já contendo os dados
     * do QRCode.
     *
     * @param paymentModel
     */
    public void publishCreatePaymentMessage(PaymentModel paymentModel) {
        rabbitTemplate.convertAndSend("payment", "", paymentModel);
    }
}
