package com.apollo.microservice.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayAuthenticationDTO {

    private String token;

    private String email;

    private boolean isValid;
}