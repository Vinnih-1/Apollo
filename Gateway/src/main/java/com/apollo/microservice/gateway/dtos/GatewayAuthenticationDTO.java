package com.apollo.microservice.gateway.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GatewayAuthenticationDTO {

    private String token;

    private boolean isValid;
}