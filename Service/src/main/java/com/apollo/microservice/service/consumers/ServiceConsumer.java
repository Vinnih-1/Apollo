package com.apollo.microservice.service.consumers;

import com.apollo.microservice.service.consumers.events.BasePaymentEvent;
import com.apollo.microservice.service.consumers.events.PaymentCreatedEvent;
import com.apollo.microservice.service.consumers.events.PaymentExpiredEvent;
import com.apollo.microservice.service.consumers.events.PaymentSuccessEvent;
import com.apollo.microservice.service.models.PaymentModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ServiceConsumer {

    private final List<BasePaymentEvent> events = Arrays.asList(
            new PaymentSuccessEvent(),
            new PaymentExpiredEvent(),
            new PaymentCreatedEvent()
    );

    @RabbitListener(queues = "${broker.queue.service.name}")
    public void listenServicePaymentQueue(@Payload PaymentModel paymentModel) {
        events.stream()
                .filter(event -> event.getEvent().equals(paymentModel.getPaymentStatus().getName()))
                .findFirst()
                .ifPresent(event -> event.execute(paymentModel));
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}