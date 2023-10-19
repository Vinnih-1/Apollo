package com.apollo.microservice.payment.services;

import com.apollo.microservice.payment.enums.PaymentStatus;
import com.apollo.microservice.payment.models.PaymentModel;
import com.apollo.microservice.payment.repositories.PaymentRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Component
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentService() {
        MercadoPagoConfig.setAccessToken(EnviromentService.getInstance().getEnv("MP_PRODUCTION_ACCESS_TOKEN"));
    }

    @SneakyThrows
    public PaymentModel generatePaymentData(PaymentModel paymentModel) {
        var request = PaymentCreateRequest.builder()
                .installments(1)
                .paymentMethodId("pix")
                .description("Serviços da Apollo")
                .transactionAmount(BigDecimal.valueOf(paymentModel.getPrice()))
                .externalReference(paymentModel.getId())
                .payer(PaymentPayerRequest.builder()
                        .email(paymentModel.getPayer())
                        .build())
                .build();

        var client = new PaymentClient();
        var response = client.create(request);
        var transactionData = response.getPointOfInteraction().getTransactionData();

        paymentModel.setQrcodeBase64(transactionData.getQrCodeBase64());
        paymentModel.setQrcode(transactionData.getQrCode());
        paymentModel.setPaymentStatus(PaymentStatus.PENDING);

        paymentRepository.saveAndFlush(paymentModel);

        return paymentModel;
    }

    public void assertCoupon(PaymentModel paymentModel) {
        var coupon = paymentModel.getCoupon();

        if (coupon == null) return;

        var value = (double) coupon.discount() / 100;
        var discountedValue = value * paymentModel.getServiceType().getPrice();
        var finalValue = Double.parseDouble(new DecimalFormat("#.00").format(
                paymentModel.getServiceType().getPrice() - discountedValue
        ));

        paymentModel.setPrice(finalValue);
    }
}