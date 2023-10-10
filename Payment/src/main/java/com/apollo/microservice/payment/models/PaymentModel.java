package com.apollo.microservice.payment.models;

import com.apollo.microservice.payment.enums.PaymentStatus;
import com.apollo.microservice.payment.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_payments")
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private ServiceType serviceType;

    @Column
    private PaymentStatus paymentStatus;

    @Column
    private double price;

    @Column
    private String payer;

    @Column
    private String coupon;

    @Column
    private Calendar createAt;

    @Column
    private Calendar expirateAt;

    @Column
    private String qrcode;

    @Column(length = 5000)
    private String qrcodeBase64;


}
