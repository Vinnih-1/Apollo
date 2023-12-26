package com.apollo.microservice.payment.dtos;

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

    private List<AuthorityDTO> authorities;

    private boolean isValid;
}