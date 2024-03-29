package com.apollo.microservice.authentication.dtos;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public record UserDTO(String email, List<SimpleGrantedAuthority> authorities) {
}
