package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.models.AuthorizationData;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.PaymentModel;
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

    @JsonIgnoreProperties({"payments", "service"})
    private List<ProductModel> products;

    @JsonIgnoreProperties({"service"})
    private List<CouponModel> coupons;

    @JsonIgnoreProperties({"qrcode", "qrcodeBase64"})
    private List<PaymentModel> payments;

    private AuthorizationData authorizationData;
}
