package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.CouponModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {

    private Long id;

    @NotNull
    private PaymentStatus paymentStatus;

    @NotNull
    @NotBlank
    private String chatId;

    @NotNull
    @NotBlank
    private String payer;

    private String externalReference;

    private ProductDTO product;

    @JsonIgnore
    private ServiceDTO service;

    private List<CouponModel> coupons;

    private Calendar createAt;

    private Calendar expirateAt;

    private String qrcode;

    private String qrcodeBase64;
}