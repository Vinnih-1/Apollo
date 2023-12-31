package com.apollo.microservice.payment.dtos;

import com.apollo.microservice.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long id;

    private PaymentStatus paymentStatus;

    private String chatId;

    private String payer;

    private String externalReference;

    private String accessToken;

    private ProductDTO product;

    private List<CouponDTO> coupons;

    private Calendar createAt;

    private Calendar expirateAt;

    private String qrcode;

    private String qrcodeBase64;
}