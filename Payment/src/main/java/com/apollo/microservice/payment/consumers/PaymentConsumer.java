package com.apollo.microservice.payment.consumers;

import com.apollo.microservice.payment.dtos.PaymentDTO;
import com.apollo.microservice.payment.producers.ServicePaymentProducer;
import com.apollo.microservice.payment.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class PaymentConsumer {

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @Autowired
    private PaymentService paymentService;

    @RabbitListener(queues = "producer.payment")
    public void listenPaymentQueue(@Payload PaymentDTO paymentDTO) {
        var expirateAt = Calendar.getInstance();
        expirateAt.add(Calendar.MINUTE, 30);

        paymentDTO = paymentService.generatePaymentData(paymentDTO);
        servicePaymentProducer.sendProductPaymentMessage(paymentDTO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}