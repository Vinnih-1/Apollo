package com.microservice.discord.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CouponDTO {

    @Column(insertable = false, updatable = false)
    private Long couponId;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Integer discount;

    @NotNull
    private Integer expirateDays;

    @Column(insertable = false, updatable = false)
    private String serviceId;

}