package com.apollo.microservice.service.models;

import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.enums.PaymentIntent;
import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "selling_products")
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private ServiceType serviceType;

    @Column
    private PaymentStatus paymentStatus;

    @Column
    private PaymentIntent paymentIntent;

    @Column
    private double price;

    @Column
    private String serviceId;

    @Column
    private String payer;

    @Column
    private Long productId;

    @Embedded
    private CouponDTO coupon;

    @Column
    private Calendar createAt;

    @Column
    private Calendar expirateAt;

    @Column
    private String qrcode;

    @Column(length = 5000)
    private String qrcodeBase64;
}