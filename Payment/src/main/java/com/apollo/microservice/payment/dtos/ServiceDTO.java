package com.apollo.microservice.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {

    private String id;

    private String owner;

    private String serviceKey;

    private List<ProductDTO> products;

    private List<CouponDTO> coupons;

    private String discordId;

    private String categoryId;

    private String accessToken;

    private boolean isSuspended;

    private Calendar createAt;

    private Calendar expirateAt;
}