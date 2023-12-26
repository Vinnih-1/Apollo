package com.apollo.microservice.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayAuthenticationDTO {

    private String token;

    private String email;

    private List<SimpleGrantedAuthority> authorities;

    private boolean isValid;
}