package com.microservice.discord.producers;

import com.microservice.discord.dtos.PaymentDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Envia a requisição de pagamento para o microsserviço
     * de Service, que agora está responsável pela geração
     * e emissão dos pagamentos, anteriormente responsável
     * pelo extinto microsserviço Payments.
     *
     * @param paymentDTO
     */
    public void publishCreatePaymentMessage(PaymentDTO paymentDTO) {
        rabbitTemplate.convertAndSend("", "producer.payment", paymentDTO);
    }
}
