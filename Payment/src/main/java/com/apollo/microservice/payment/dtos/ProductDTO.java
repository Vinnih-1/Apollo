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
public class ProductDTO {

    private Long id;

    private String name;

    private double price;

    private List<PaymentDTO> payments;

    private ServiceDTO service;

    private String serviceId;

    private String description;

    private Calendar createAt;
}
