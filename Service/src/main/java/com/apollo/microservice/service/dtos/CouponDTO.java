package com.apollo.microservice.service.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Data
public class CouponDTO {

    @Column(insertable = false, updatable = false)  private Long couponId;

    @NotNull @NotBlank private String name;

    @NotNull private Integer discount;

    @NotNull private Integer expirateDays;

    @Column(insertable = false, updatable = false) private String serviceId;

}