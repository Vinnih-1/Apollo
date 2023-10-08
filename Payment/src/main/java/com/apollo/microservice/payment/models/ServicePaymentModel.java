package com.apollo.microservice.payment.models;

import com.apollo.microservice.payment.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_payments")
public class ServicePaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private ServiceType serviceType;

    @Column
    private String payer;

    @Column
    private String qrcode;

    @Column(length = 5000)
    private String qrcodeBase64;
}
