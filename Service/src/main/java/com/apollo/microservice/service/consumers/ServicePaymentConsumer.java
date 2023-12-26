package com.apollo.microservice.service.consumers;

import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.producers.ServicePaymentProducer;
import com.apollo.microservice.service.repositories.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentConsumer {

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @Autowired
    private PaymentRepository paymentRepository;

    @RabbitListener(queues = "payment.service")
    public void listenServicePaymentQueue(@Payload PaymentModel paymentModel) {
        if (paymentModel.getExternalReference() == null) return;

        paymentRepository.findByExternalReference(paymentModel.getExternalReference())
                .ifPresentOrElse(payment -> {
                    payment.setPaymentStatus(paymentModel.getPaymentStatus());
                    paymentRepository.save(payment);
                    System.out.println("Novo pagamento editado: " + payment);
                }, () -> {
                    paymentRepository.save(paymentModel);
                    System.out.println("Novo pagamento criado: " + paymentModel);
                });
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}