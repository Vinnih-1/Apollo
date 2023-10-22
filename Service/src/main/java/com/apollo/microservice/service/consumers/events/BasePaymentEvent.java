package com.apollo.microservice.service.consumers.events;

import com.apollo.microservice.service.models.PaymentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BasePaymentEvent {
    
    private final String event;
    
    public abstract void execute(PaymentModel paymentModel);
}