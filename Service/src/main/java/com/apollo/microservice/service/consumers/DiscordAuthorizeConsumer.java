package com.apollo.microservice.service.consumers;

import com.apollo.microservice.service.dtos.AuthorizeDTO;
import com.apollo.microservice.service.models.AuthorizeModel;
import com.apollo.microservice.service.producers.PaymentAuthorizeProducer;
import com.apollo.microservice.service.repositories.AuthorizeRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class DiscordAuthorizeConsumer {

    @Autowired
    private PaymentAuthorizeProducer paymentAuthorizeProducer;

    @Autowired
    private AuthorizeRepository authorizeRepository;

    @RabbitListener(queues = "${broker.queue.auth.discord}")
    public void listenDiscordResponseQueue(@Payload AuthorizeDTO authorizeDTO) {
        var expirate = Calendar.getInstance();
        expirate.add(Calendar.MINUTE, 30);

        var authorizeModel = authorizeRepository.findByServiceId(authorizeDTO.serviceId())
                .orElseGet(() -> {
                    var newAuthorize = new AuthorizeModel(authorizeDTO);

                    newAuthorize.setExpirateAt(expirate);
                    authorizeRepository.saveAndFlush(newAuthorize);

                    return newAuthorize;
                });
        paymentAuthorizeProducer.publishPaymentAuthorizeRequest(authorizeModel);
    }

    @RabbitListener(queues = "${broker.queue.auth.result}")
    public void listenAuthorizationResult(@Payload AuthorizeModel authorizeModel) {
        var authorizate = authorizeRepository.findByServiceId(authorizeModel.getServiceId()).orElse(null);

        if (authorizate == null) return;

        authorizate.setAuthorizeUrl(authorizeModel.getAuthorizeUrl());
        authorizate.setAuthStatus(authorizeModel.getAuthStatus());
        authorizate.setDiscordId(authorizeModel.getDiscordId());
        authorizeRepository.saveAndFlush(authorizate);
        paymentAuthorizeProducer.publishPaymentAuthorizeResponse(authorizate);
    }
}