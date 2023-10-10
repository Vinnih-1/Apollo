package com.apollo.microservice.service.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Calendar;

@Data
@Builder
@Entity
@Table(name = "coupons_service")
public class CouponModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String serviceId;

    @Column
    private Integer discount;

    @Column
    private boolean isEnabled;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Temporal(TemporalType.DATE)
    private Calendar expirateAt;
}