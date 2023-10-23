package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.enums.AuthStatus;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.producers.PaymentAuthorizeProducer;
import com.apollo.microservice.service.repositories.AuthorizeRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import com.apollo.microservice.service.services.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private AuthorizeRepository authorizeRepository;

    @Autowired
    private PaymentAuthorizeProducer paymentAuthorizeProducer;

    @GetMapping("/")
    public ResponseEntity<ServiceModel> authenticationService(
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
        serviceRepository.saveAndFlush(service);
        authorizeRepository.findByServiceId(state)
                .ifPresent(authorizeModel -> {
                    authorizeModel.setAuthStatus(AuthStatus.AUTH_SUCCESS);
                    paymentAuthorizeProducer.publishPaymentAuthorizeResponse(authorizeModel);
                    authorizeRepository.delete(authorizeModel);
                });

        return ResponseEntity.ok().build();
    }
}