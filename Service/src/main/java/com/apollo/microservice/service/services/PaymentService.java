package com.apollo.microservice.service.services;

import com.apollo.microservice.service.configs.DotEnv;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.PaymentRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private DotEnv dotEnv;

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentModel findByExternalReference(String externalReference) {
        return paymentRepository.findByExternalReference(externalReference).orElse(null);
    }

    public Page<PaymentModel> getPageablePayments(Pageable pageable) {
        return paymentRepository.findAllNonExpired(pageable);
    }

    public Page<PaymentModel> getPageablePaymentsByServiceId(Pageable pageable, String serviceId) {
        return paymentRepository.findAllByServiceId(pageable, serviceId);
    }

    public String generateAuthorizationUrl(ServiceModel serviceModel) {
        var client = new OauthClient();

        try {
            var authorizationUrl = client.getAuthorizationURL(
                    dotEnv.get("APP_ID"),
                    dotEnv.get("REDIRECT_URL"),
                    MPRequestOptions.builder()
                            .accessToken(dotEnv.get("ACCESS_TOKEN"))
                            .build()
            );

            return authorizationUrl + "&state=" + serviceModel.getId();
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateAccessToken(String authCode) {
        var client = new OauthClient();
        try {
            var result = client.createCredential(authCode, dotEnv.get("REDIRECT_URL"),
                    MPRequestOptions.builder().accessToken(dotEnv.get("ACCESS_TOKEN")).build());

            return result.getAccessToken();
        } catch (MPApiException e) {
            System.err.println(e.getApiResponse().getContent());
            return null;
        } catch (MPException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal calculateCouponDiscount(CouponModel couponModel, ProductModel productModel) {
        if (couponModel == null) return BigDecimal.valueOf(productModel.getPrice());
        var discount = (double) couponModel.getDiscount() / 100;
        var discountValue = productModel.getPrice() - (productModel.getPrice() * discount);
        return new BigDecimal(discountValue).setScale(2, RoundingMode.HALF_UP);
    }

    public PaymentCreateRequest generatePaymentRequest(PaymentModel paymentModel) {
        var uuid = UUID.randomUUID();
        var discount = calculateCouponDiscount(paymentModel.getCoupon(), paymentModel.getProduct());
        paymentModel.getProduct().setPrice(discount.doubleValue());
        var request = PaymentCreateRequest.builder()
                .installments(1)
                .notificationUrl("https://payments.apollodiscord.com/notification?cliente=" + paymentModel.getId())
                .paymentMethodId("pix")
                .description("Serviços da Apollo")
                .transactionAmount(discount)
                .externalReference(uuid.toString())
                .payer(PaymentPayerRequest.builder()
                        .email(paymentModel.getPayer())
                        .build());

        return request.build();
    }

    public PaymentModel generatePaymentData(PaymentModel paymentModel, ServiceModel serviceModel) {
        MercadoPagoConfig.setAccessToken(serviceModel.getAuthorizationData().getAccessToken());
        var request = generatePaymentRequest(paymentModel);
        var client = new PaymentClient();

        try {
            var response = client.create(request);
            var transactionData = response.getPointOfInteraction().getTransactionData();
            var expirate = Calendar.getInstance();
            expirate.add(Calendar.MINUTE, 30);

            paymentModel.setQrcodeBase64(transactionData.getQrCodeBase64());
            paymentModel.setQrcode(transactionData.getQrCode());
            paymentModel.setExternalReference(request.getExternalReference());
            paymentModel.setCreateAt(Calendar.getInstance());
            paymentModel.setExpirateAt(expirate);

            paymentRepository.saveAndFlush(paymentModel);
            return paymentModel;
        } catch (MPApiException e) {
            System.err.println(e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}