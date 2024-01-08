package com.apollo.microservice.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayAuthenticationDTO {

    private String token;

    private String email;

    private List<Authority> authorities;

    private boolean isValid;
}