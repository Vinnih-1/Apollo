package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.enums.PaymentIntent;
import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.producers.ServicePaymentProducer;
import com.apollo.microservice.service.repositories.PaymentRepository;
import com.apollo.microservice.service.repositories.ProductRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@RequestMapping("service/product")
public class ProductController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @GetMapping("{id}/payment")
    public ResponseEntity<PaymentModel> generateProductPayment(
            @PathVariable(value = "id") Long id,
            @RequestParam("payer") String payer,
            @RequestParam("chatId") String chatId) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null)
            return ResponseEntity.badRequest().build();

        var service = serviceRepository.findById(product.getServiceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().build();

        var expirateAt = Calendar.getInstance();
        expirateAt.add(Calendar.MINUTE, 30);

        var paymentModel = PaymentModel.builder()
                .payer(payer)
                .productId(id)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentIntent(PaymentIntent.SELL_PRODUCT)
                .createAt(Calendar.getInstance())
                .expirateAt(expirateAt)
                .serviceId(service.getId())
                .chatId(chatId)
                .accessToken(service.getAccessToken())
                .coupon(null)
                .build();

        paymentRepository.saveAndFlush(paymentModel);
        servicePaymentProducer.publishCreatePaymentMessage(paymentModel);

        return ResponseEntity.ok(paymentModel);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductModel> createServiceProduct(@Valid @RequestBody ProductDTO productDTO) {
        var service = serviceRepository.findById(productDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado").build();

        var product = ProductModel.builder()
                .name(productDTO.name())
                .description(productDTO.description())
                .price(productDTO.price())
                .serviceId(productDTO.serviceId())
                .createAt(Calendar.getInstance())
                .build();

        productRepository.saveAndFlush(product);
        serviceRepository.saveAndFlush(service);

        return ResponseEntity.status(204).body(product);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteServiceProduct(@Valid @RequestBody ProductDTO productDTO) {
        var service = serviceRepository.findById(productDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        var product = productRepository.findById(productDTO.id()).orElse(null);

        if (product == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este produto não foi encontrado!").build();

        productRepository.delete(product);
        serviceRepository.saveAndFlush(service);

        return ResponseEntity.status(204).build();
    }
}