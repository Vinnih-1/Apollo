package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.enums.PaymentIntent;
import com.apollo.microservice.service.enums.PaymentStatus;

public record PaymentDTO(
        String id,
        String payer,
        String serviceId,
        CouponDTO coupon,
        PaymentStatus paymentStatus,
        PaymentIntent paymentIntent,
        double price,
        Long productId
) {
}
