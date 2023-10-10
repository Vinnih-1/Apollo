package com.apollo.microservice.payment.dto;

import com.apollo.microservice.payment.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public record PaymentDTO(@NotNull @NotBlank String payer, String coupon, @NotNull ServiceType serviceType) {

}
