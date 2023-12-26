package com.apollo.microservice.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(@NotBlank @NotNull @Email String email, @NotBlank @NotNull String password) {
}
