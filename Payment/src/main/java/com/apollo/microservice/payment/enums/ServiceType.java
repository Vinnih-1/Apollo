package com.apollo.microservice.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceType {

    BEGGINER(29.99), PROFESSIONAL(49.99);

    private final double price;
}
