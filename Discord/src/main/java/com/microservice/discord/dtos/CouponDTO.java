package com.microservice.discord.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Calendar;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponDTO {

    @Column(insertable = false, updatable = false)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Integer discount;

    private Integer usage;

    private Integer maxUsage;

    private boolean isExpired;

    @NotNull
    private Integer expirateDays;

    @Column(insertable = false, updatable = false)
    private String serviceId;

    private Calendar createAt;

    private Calendar expirateAt;
}