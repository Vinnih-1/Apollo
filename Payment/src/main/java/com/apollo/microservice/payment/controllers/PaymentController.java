package com.apollo.microservice.payment.controllers;

import com.apollo.microservice.payment.dto.PaymentDTO;
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
@RequestMapping("payment/service")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/generate")
    public ResponseEntity<PaymentModel> generatePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);

        if (paymentRepository.findByPayer(paymentDTO.payer()).isPresent())
            return ResponseEntity.ok(paymentRepository.findByPayer(paymentDTO.payer()).get());

        var payment = PaymentModel.builder()
                .payer(paymentDTO.payer())
                .price(paymentDTO.serviceType().getPrice())
                .serviceType(paymentDTO.serviceType())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .build();

        paymentService.assertCoupon(payment, paymentDTO.coupon());

        return ResponseEntity.ok(paymentService.generatePaymentData(payment));
    }


}