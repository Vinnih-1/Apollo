package com.apollo.microservice.payment.services;

import com.apollo.microservice.payment.enums.PaymentIntent;
import com.apollo.microservice.payment.enums.PaymentStatus;
import com.apollo.microservice.payment.models.PaymentModel;
import com.apollo.microservice.payment.producers.ServicePaymentProducer;
import com.apollo.microservice.payment.repositories.PaymentRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private static final String APP_ID = "5221114361860271";

    private static final String REDIRECT_URL = "https://service.apollodiscord.com";

    private static final String ACCESS_TOKEN = "APP_USR-5221114361860271-092410-1e60ee4d56797285791a0d9145f917fa-1187350395";

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentModel generatePaymentData(PaymentModel paymentModel) {
        if (paymentModel.getAccessToken() != null && paymentModel.getPaymentIntent() == PaymentIntent.SELL_PRODUCT)
            MercadoPagoConfig.setAccessToken(paymentModel.getAccessToken());
        else
            MercadoPagoConfig.setAccessToken(EnviromentService.getInstance().getEnv("MP_PRODUCTION_ACCESS_TOKEN"));

        var request = PaymentCreateRequest.builder()
                .installments(1)
                .notificationUrl("https://payments.apollodiscord.com/notification?cliente=" + paymentModel.getId())
                .paymentMethodId("pix")
                .description("Serviços da Apollo")
                .transactionAmount(BigDecimal.valueOf(paymentModel.getPrice()))
                .externalReference(paymentModel.getId())
                .payer(PaymentPayerRequest.builder()
                        .email(paymentModel.getPayer())
                        .build())
                .build();

        var client = new PaymentClient();

        try {
            var response = client.create(request);
            var transactionData = response.getPointOfInteraction().getTransactionData();

            paymentModel.setQrcodeBase64(transactionData.getQrCodeBase64());
            paymentModel.setQrcode(transactionData.getQrCode());
            paymentModel.setPaymentStatus(PaymentStatus.PENDING);
            paymentModel.setExternalReference(paymentModel.getId());

            paymentRepository.saveAndFlush(paymentModel);

            return paymentModel;
        } catch (MPApiException e) {
            System.err.println(e.getApiResponse().getContent());
            return null;
        } catch (MPException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePaymentStatus(String externalReference, String id) {
        var paymentModel = paymentRepository.findByExternalReference(externalReference).orElse(null);

        if (paymentModel == null) return;
        var client = new PaymentClient();

        try {
            System.out.println("Payment ID: " + Long.parseLong(id));
            var response = client.get(Long.parseLong(id), MPRequestOptions.builder()
                    .accessToken(paymentModel.getAccessToken())
                    .build());

            if (response.getStatus().equals("approved")) {
                paymentModel.setPaymentStatus(PaymentStatus.PAYED);
                servicePaymentProducer.sendProductPaymentMessage(paymentModel);
                paymentRepository.delete(paymentModel);
            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            System.err.println(e.getApiResponse().getContent());
        }
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1L)
    public void deleteExpiredPayments() {
        var calendar = Calendar.getInstance();
        var payments = paymentRepository.findExpiredPayments(calendar);

        payments.stream()
                .filter(payment -> payment.getPaymentIntent() == PaymentIntent.SELL_PRODUCT)
                .forEach(payment -> {
                    payment.setPaymentStatus(PaymentStatus.EXPIRED);
                    servicePaymentProducer.sendProductPaymentMessage(payment);
                });
        paymentRepository.deleteAll(payments);
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