package com.apollo.microservice.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public record CouponDTO(@NotNull @NotBlank String name, @NotNull Integer discount, @NotNull Integer expirateDays) {

}
