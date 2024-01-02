package com.apollo.microservice.service.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column
    private Integer discount;

    @ManyToOne
    @JsonIgnoreProperties("coupons")
    @JoinColumn(name = "service_id")
    private ServiceModel service;

    @Column(name = "coupon_usage")
    private Integer usage;

    @Column
    private Integer maxUsage;

    @Column
    private boolean isExpired;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Temporal(TemporalType.DATE)
    private Calendar expirateAt;

    @Override
    public String toString() {
        return "CouponModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discount=" + discount +
                ", usage=" + usage +
                ", maxUsage=" + maxUsage +
                ", isExpired=" + isExpired +
                ", createAt=" + createAt +
                ", expirateAt=" + expirateAt +
                '}';
    }
}