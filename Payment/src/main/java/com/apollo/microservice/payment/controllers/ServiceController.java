package com.apollo.microservice.payment.controllers;

import com.apollo.microservice.payment.dtos.PaymentDTO;
import com.apollo.microservice.payment.enums.PaymentIntent;
import com.apollo.microservice.payment.models.PaymentModel;
import com.apollo.microservice.payment.repositories.PaymentRepository;
import com.apollo.microservice.payment.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@RequestMapping("/payment/service")
public class ServiceController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/generate")
    public ResponseEntity<PaymentModel> generatePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);

        if (paymentRepository.findByPayer(paymentDTO.payer()).isPresent()) {
            var payment = paymentRepository.findByPayer(paymentDTO.payer()).get();
            if (payment.getPaymentIntent() == PaymentIntent.SELL_PRODUCT)
                return ResponseEntity.ok(payment);
        }

        var payment = PaymentModel.builder()
                .payer(paymentDTO.payer())
                .price(paymentDTO.price())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .build();

        paymentService.assertCoupon(payment);

        return ResponseEntity.ok(paymentService.generatePaymentData(payment));
    }
}