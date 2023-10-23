package com.apollo.microservice.payment.consumers.events;

import com.apollo.microservice.payment.dtos.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseEvent {

    private final String event;

    public abstract void execute(PaymentDTO paymentDTO);
}