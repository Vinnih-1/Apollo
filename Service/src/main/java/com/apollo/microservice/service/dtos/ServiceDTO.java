package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.models.AuthorizationData;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.ProductModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDTO {

    private String id;

    private String serviceKey;

    @NotNull
    @NotBlank
    private String owner;

    private long buyers;

    private double moneyMoved;

    private List<ProductModel> products;

    private List<CouponModel> coupons;

    private AuthorizationData authorizationData;
}
