package com.apollo.microservice.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        Long id,
        @NotNull @NotBlank String name,
        String description,
        @NotNull double price,
        @NotNull @NotBlank String serviceId) {
}