package com.apollo.microservice.payment.dto;

import com.apollo.microservice.payment.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentDTO {

    @NotNull
    @NotBlank
    private final String payer;

    private final String coupon;

    @NotNull
    private final ServiceType serviceType;
}
