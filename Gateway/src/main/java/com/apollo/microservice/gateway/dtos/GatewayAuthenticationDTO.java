package com.apollo.microservice.gateway.dtos;

public record GatewayAuthenticationDTO(String token, String email, boolean valid) {
}