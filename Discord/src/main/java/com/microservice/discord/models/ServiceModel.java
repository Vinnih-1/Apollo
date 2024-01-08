package com.microservice.discord.models;

import com.microservice.discord.dtos.CouponDTO;
import com.microservice.discord.dtos.ProductDTO;
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
public class ServiceModel {

    private String id;

    private String owner;

    private String serviceKey;

    private List<ProductDTO> products;

    private List<CouponDTO> coupons;

    private AuthorizationData authorizationData;

    private Calendar createAt;
}