package com.microservice.discord.models;

import com.microservice.discord.dtos.CouponDTO;
import com.microservice.discord.dtos.ProductDTO;
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
public class PaymentModel {

    private Long id;

    private PaymentStatus paymentStatus;

    private String chatId;

    private String payer;

    private String externalReference;

    private ProductDTO product;

    private CouponDTO coupon;

    private AuthorizationData authorizationData;

    private Calendar createAt;

    private Calendar expirateAt;

    private String qrcode;

    private String qrcodeBase64;
}