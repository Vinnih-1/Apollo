package com.microservice.discord.dtos;

import com.microservice.discord.enums.PaymentIntent;
import com.microservice.discord.enums.PaymentStatus;

public record PaymentDTO(
        String id,
        String payer,
        String discordId,
        String chatId,
        String couponId,
        PaymentStatus paymentStatus,
        PaymentIntent paymentIntent,
        double price,
        Long productId
) {
}
