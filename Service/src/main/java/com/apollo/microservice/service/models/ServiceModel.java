package com.apollo.microservice.service.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "services")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String owner;

    @Column
    private String pixKey;

    @Column
    private String discordId;

    @Column
    private String accessToken;

    @Column
    private boolean isSuspended;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ProductModel> products;

    @OneToMany(fetch = FetchType.EAGER)
    private List<CouponModel> coupons;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Temporal(TemporalType.DATE)
    private Calendar expirateAt;
}