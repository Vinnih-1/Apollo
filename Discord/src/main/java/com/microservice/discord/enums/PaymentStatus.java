package com.microservice.discord.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    PENDING("payment-created"), PAYED("payment-success"), EXPIRED("payment-expired");

    private final String name;
}