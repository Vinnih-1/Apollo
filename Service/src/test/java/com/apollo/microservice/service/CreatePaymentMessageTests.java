package com.apollo.microservice.service;

import com.apollo.microservice.service.enums.PaymentIntent;
import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.producers.ServicePaymentProducer;
import com.apollo.microservice.service.repositories.CouponRepository;
import com.apollo.microservice.service.repositories.PaymentRepository;
import com.apollo.microservice.service.repositories.ProductRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class CreatePaymentMessageTests {

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void contextLoads() {
        var service = ServiceModel.builder()
                .owner("viniciusalb20@gmail.com")
                .discordId("230184912839128313131")
                .accessToken("9218349012877390123781029371")
                .pixKey("2193481709347120937182031")
                .createAt(Calendar.getInstance())
                .expirateAt(Calendar.getInstance())
                .isSuspended(false)
                .build();

        serviceRepository.saveAndFlush(service);

        var coupon = CouponModel.builder()
                .name("NATAL40")
                .serviceId(service.getId())
                .isEnabled(true)
                .discount(40)
                .createAt(Calendar.getInstance())
                .expirateAt(Calendar.getInstance())
                .build();

        var product = ProductModel.builder()
                .name("VIP")
                .price(1.0)
                .description("Obtenha itens e vantagem")
                .createAt(Calendar.getInstance())
                .serviceId(service.getId())
                .build();

        productRepository.saveAndFlush(product);

        var payment = PaymentModel.builder()
                .payer(service.getOwner())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentIntent(PaymentIntent.SELL_PRODUCT)
                .expirateAt(Calendar.getInstance())
                .createAt(Calendar.getInstance())
                .productId(product.getId())
                .coupon(null)
                .productId(product.getId())
                .build();

        paymentRepository.saveAndFlush(payment);

        servicePaymentProducer.publishCreatePaymentMessage(payment);
    }
}