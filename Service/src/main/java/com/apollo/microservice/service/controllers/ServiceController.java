package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.dtos.ServiceDTO;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.ProductRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<ServiceModel> createService(@Valid @RequestBody ServiceDTO serviceDTO) {
        if (serviceRepository.findByOwner(serviceDTO.owner()).isPresent())
            return ResponseEntity.badRequest().header("Error-Message", "Este usuário já tem um serviço!").build();

        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        var service = ServiceModel.builder()
                .owner(serviceDTO.owner())
                .serviceKey(RandomStringUtils.randomAlphanumeric(8))
                .discordId(serviceDTO.discordId())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .isSuspended(false)
                .build();

        serviceRepository.saveAndFlush(service);

        return ResponseEntity.ok(service);
    }

    @PutMapping("/edit")
    public ResponseEntity<ServiceModel> editService(@Valid @RequestBody ServiceDTO serviceDTO) {
        var service = serviceRepository.findById(serviceDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        service.setDiscordId(serviceDTO.discordId());
        serviceRepository.saveAndFlush(service);

        return ResponseEntity.ok(service);
    }

    @PutMapping("/suspend")
    public ResponseEntity<ServiceModel> suspendService(@Valid @RequestBody ServiceDTO serviceDTO) {
        var service = serviceRepository.findById(serviceDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        service.setSuspended(true);
        serviceRepository.saveAndFlush(service);

        return ResponseEntity.ok(service);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceByServiceId(@PathVariable(value = "id") String id) {
        var service = serviceRepository.findById(id).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().build();

        var products = productRepository.findProductsByServiceId(service.getId()).orElse(null)
                .stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        service.getId())
                ).toList();

        var serviceDto = new ServiceDTO(
                service.getId(),
                service.getOwner(),
                service.getDiscordId(),
                service.getCategoryId(),
                null,
                products
        );

        return ResponseEntity.ok(serviceDto);
    }
    @GetMapping("/discord/{id}")
    public ResponseEntity<ServiceDTO> getServiceByDiscordId(@PathVariable(value = "id") String id) {
        var service = serviceRepository.findByDiscordId(id).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().build();

        var products = productRepository.findProductsByServiceId(service.getId()).orElse(null)
                .stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        service.getId())
                ).toList();

        var serviceDto = new ServiceDTO(
                service.getId(),
                service.getOwner(),
                service.getDiscordId(),
                service.getCategoryId(),
                null,
                products
        );

        return ResponseEntity.ok(serviceDto);
    }
}