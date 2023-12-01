package com.apollo.microservice.authentication.services;

import com.apollo.microservice.authentication.entities.UserCredentials;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("secret-key")
    private String secretKey;

    public String generateToken(UserCredentials userCredentials) {
        return JWT.create()
                .withIssuer("user")
                .withSubject(userCredentials.getEmail())
                .withClaim("id", userCredentials.getId())
                .withExpiresAt(
                        LocalDateTime.now()
                                .plusDays(30)
                                .toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256(secretKey));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .withIssuer("user")
                .build().verify(token).getSubject();
    }
}