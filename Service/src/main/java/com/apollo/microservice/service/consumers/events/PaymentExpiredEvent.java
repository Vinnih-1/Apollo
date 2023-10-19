package com.apollo.microservice.service.consumers.events;

import com.apollo.microservice.service.models.PaymentModel;

public class PaymentExpiredEvent extends BaseEvent {

    public PaymentExpiredEvent() {
        super("payment-expired");
    }

    @Override
    public void execute(PaymentModel paymentModel) {
        System.out.println("pagamento expirado");
    }
}