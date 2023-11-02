package com.apollo.microservice.payment.dtos;

import com.apollo.microservice.payment.enums.PaymentIntent;
import com.apollo.microservice.payment.enums.PaymentStatus;

public record PaymentDTO(
        String id,
        String payer,
        String serviceId,
        String chatId,
        String accessToken,
        CouponDTO coupon,
        PaymentStatus paymentStatus,
        PaymentIntent paymentIntent,
        double price,
        Long productId
) {
}