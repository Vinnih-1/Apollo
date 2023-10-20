package com.apollo.microservice.service.consumers.events;

import com.apollo.microservice.service.models.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentSuccessEvent extends BaseEvent {

    // inserir registro de pagamento no banco de dados

    public PaymentSuccessEvent() {
        super("payment-success");
    }

    @Override
    public void execute(PaymentModel paymentModel) {
        System.out.println("Pagando Aprovado: " + paymentModel.toString());
    }
}
