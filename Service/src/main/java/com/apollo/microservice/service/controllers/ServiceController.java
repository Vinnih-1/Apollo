package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.dtos.ServiceDTO;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;

@RestController
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @PostMapping("/create")
    public ResponseEntity<ServiceModel> createService(@Valid @RequestBody ServiceDTO serviceDTO) {
        if (serviceRepository.findByOwner(serviceDTO.owner()).isPresent())
            return ResponseEntity.badRequest().header("Error-Message", "Este usuário já tem um serviço!").build();

        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        var service = ServiceModel.builder()
                .owner(serviceDTO.owner())
                .pixKey(serviceDTO.pixKey())
                .discordId(serviceDTO.discordId())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .products(new ArrayList<>())
                .coupons(new ArrayList<>())
                .build();

        serviceRepository.saveAndFlush(service);

        return ResponseEntity.status(204).body(service);
    }


}