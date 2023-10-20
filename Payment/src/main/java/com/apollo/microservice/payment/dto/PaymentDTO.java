package com.apollo.microservice.payment.dto;

import com.apollo.microservice.payment.enums.PaymentIntent;
import com.apollo.microservice.payment.enums.PaymentStatus;
import com.apollo.microservice.payment.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentDTO(
        String id,
        String payer,
        String serviceId,
        String accessToken,
        CouponDTO coupon,
        PaymentStatus paymentStatus,
        PaymentIntent paymentIntent,
        double price,
        Long productId
) {
}