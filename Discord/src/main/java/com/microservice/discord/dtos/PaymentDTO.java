package com.microservice.discord.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservice.discord.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {

    private Long id;

    private PaymentStatus paymentStatus;

    private String chatId;

    private String payer;

    private String externalReference;

    private ProductDTO product;

    private CouponDTO coupon;

    private Calendar createAt;

    private Calendar expirateAt;

    private String qrcode;

    private String qrcodeBase64;
}