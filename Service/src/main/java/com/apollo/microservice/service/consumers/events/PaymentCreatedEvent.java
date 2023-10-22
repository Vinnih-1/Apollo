package com.apollo.microservice.service.consumers.events;

import com.apollo.microservice.service.models.PaymentModel;

public class PaymentCreatedEvent extends BasePaymentEvent {

    public PaymentCreatedEvent() {
        super("payment-created");
    }

    @Override
    public void execute(PaymentModel paymentModel) {
        System.out.println("Pagamento criado: " + paymentModel.toString());
    }
}
