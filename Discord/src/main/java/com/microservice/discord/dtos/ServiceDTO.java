package com.microservice.discord.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservice.discord.models.AuthorizationData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDTO {

    private String id;

    private String serviceKey;

    private String owner;

    private long buyers;

    private double moneyMoved;

    private List<ProductDTO> products;

    private List<CouponDTO> coupons;

    private AuthorizationData authorizationData;
}