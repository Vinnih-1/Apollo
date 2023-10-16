package com.apollo.microservice.service.services;

import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.stereotype.Component;

@Component
public class MercadoPagoService {

    private static final String ACCESS_TOKEN = "YOUR_APPLICATION_ACCESS_TOKEN";

    private static final String REDIRECT_URL = "https://loja.apollodiscord.com";

    public String generateAccessToken(String authCode) {
        var client = new OauthClient();
        try {
            var result = client.createCredential(authCode, REDIRECT_URL,
                    MPRequestOptions.builder().accessToken(ACCESS_TOKEN).build());

            return result.getAccessToken();
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
    }
}