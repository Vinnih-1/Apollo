package com.apollo.microservice.service.models;

import com.apollo.microservice.service.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_payments")
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private PaymentStatus paymentStatus;

    @Column
    private String chatId;

    @Column
    private String payer;

    @Column
    private String externalReference;

    @Embedded
    private AuthorizationData authorizationData;

    @ManyToOne
    @JsonIgnoreProperties({"payments", "service"})
    private ProductModel product;

    @OneToMany(fetch = FetchType.EAGER)
    private List<CouponModel> coupons;

    @Column
    private Calendar createAt;

    @Column
    private Calendar expirateAt;

    @Column
    private String qrcode;

    @Column(length = 10000)
    private String qrcodeBase64;

    @Override
    public String toString() {
        return "PaymentModel{" +
                "id=" + id +
                ", paymentStatus=" + paymentStatus +
                ", chatId='" + chatId + '\'' +
                ", payer='" + payer + '\'' +
                ", externalReference='" + externalReference + '\'' +
                ", authorizationData=" + authorizationData +
                ", product=" + product +
                ", createAt=" + createAt +
                ", expirateAt=" + expirateAt +
                ", qrcode='" + qrcode + '\'' +
                ", qrcodeBase64='" + qrcodeBase64 + '\'' +
                '}';
    }
}