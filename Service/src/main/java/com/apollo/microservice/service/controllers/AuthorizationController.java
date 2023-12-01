package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.enums.AuthStatus;
import com.apollo.microservice.service.models.AuthorizeModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.producers.PaymentAuthorizeProducer;
import com.apollo.microservice.service.repositories.AuthorizeRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import com.apollo.microservice.service.services.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@RequestMapping("/service")
public class AuthorizationController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private AuthorizeRepository authorizeRepository;

    @Autowired
    private PaymentAuthorizeProducer paymentAuthorizeProducer;

    @GetMapping("/authorize")
    public ResponseEntity<AuthorizeModel> authorizationRequest(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("discordId") String discordId,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("chatId") String chatId
    ) {
        if (!serviceRepository.existsById(serviceId) || !serviceRepository.existsByServiceKey(serviceKey))
            return ResponseEntity.badRequest().build();

        var savedAuthorization = authorizeRepository.findByServiceId(serviceId).orElse(null);

        if (savedAuthorization != null)
            authorizeRepository.delete(savedAuthorization);

        var expirateAt = Calendar.getInstance();
        expirateAt.add(Calendar.MINUTE, 30);

        var authorizeModel = AuthorizeModel.builder()
                .authStatus(AuthStatus.AUTH_PENDING)
                .serviceId(serviceId)
                .serviceKey(serviceKey)
                .discordId(discordId)
                .categoryId(categoryId)
                .chatId(chatId)
                .expirateAt(expirateAt)
                .build();

        authorizeModel.setAuthorizeUrl(mercadoPagoService.generateAuthorizationUrl(authorizeModel));
        authorizeRepository.saveAndFlush(authorizeModel);

        return ResponseEntity.ok(authorizeModel);
    }

    @GetMapping("/validate")
    public ResponseEntity<ServiceModel> authorizationService(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        var service = serviceRepository.findById(state).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();
        var accessToken = mercadoPagoService.generateAccessToken(code);

        if (accessToken == null)
            return ResponseEntity.badRequest().header("Error-Message", "Não foi possível gerar o Access Token!").build();

        service.setAccessToken(accessToken);
        authorizeRepository.findByServiceId(state)
                .ifPresent(authorizeModel -> {
                    authorizeModel.setAuthStatus(AuthStatus.AUTH_SUCCESS);
                    service.setDiscordId(authorizeModel.getDiscordId());
                    service.setCategoryId(authorizeModel.getCategoryId());
                    paymentAuthorizeProducer.publishPaymentAuthorizeResponse(authorizeModel);
                    authorizeRepository.delete(authorizeModel);
                });
        serviceRepository.saveAndFlush(service);

        return ResponseEntity.ok().build();
    }
}