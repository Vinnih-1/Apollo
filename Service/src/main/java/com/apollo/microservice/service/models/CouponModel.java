package com.apollo.microservice.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_coupons")
public class CouponModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @ManyToOne
    private PaymentModel payment;

    @Column
    private Integer discount;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "service_id")
    private ServiceModel service;

    @Column
    private Integer usage;

    @Column
    private Integer maxUsage;

    @Column
    private boolean isExpired;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Temporal(TemporalType.DATE)
    private Calendar expirateAt;
}