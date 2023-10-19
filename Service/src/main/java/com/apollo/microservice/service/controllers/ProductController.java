package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.models.ProductModel;
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
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

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