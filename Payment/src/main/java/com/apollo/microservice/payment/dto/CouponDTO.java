package com.apollo.microservice.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponDTO {

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    private final Integer discount;

    @NotNull
    private final Integer expirateDays;
}
