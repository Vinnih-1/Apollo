package com.microservice.discord.models;

import com.microservice.discord.dtos.CouponDTO;
import com.microservice.discord.enums.PaymentIntent;
import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel {

    private String id;

    private ServiceType serviceType;

    private PaymentStatus paymentStatus;

    private PaymentIntent paymentIntent;

    private double price;

    private String serviceId;

    private String chatId;

    private String payer;

    private String externalReference;

    private String accessToken;

    private Long productId;

    private CouponDTO coupon;

    private String createAt;

    private String expirateAt;

    private String qrcode;

    private String qrcodeBase64;
}