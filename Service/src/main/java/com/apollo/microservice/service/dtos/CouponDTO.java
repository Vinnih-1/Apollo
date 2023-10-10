package com.apollo.microservice.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponDTO(
        Long id,
        @NotNull @NotBlank String name,
        @NotNull Integer discount,
        @NotNull Integer expirateDays,
        @NotNull @NotBlank String serviceId) {
}