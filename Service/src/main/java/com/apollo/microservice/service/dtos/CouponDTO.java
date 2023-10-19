package com.apollo.microservice.service.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Embeddable
public record CouponDTO(@Column(insertable = false, updatable = false) Long couponId,
                        @NotNull @NotBlank String name,
                        @NotNull Integer discount,
                        @NotNull Integer expirateDays,
                        @Column(insertable = false, updatable = false) String serviceId) {
}