package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.clients.UserClient;
import com.apollo.microservice.service.dtos.Authority;
import com.apollo.microservice.service.dtos.ServiceDTO;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.services.PlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserClient userClient;

    @Autowired
    private PlanService planService;

    @GetMapping("/")
    public ResponseEntity<ServiceDTO> getServiceByToken(@RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        var service = planService.findByOwner(user.getEmail());
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        var serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(service, serviceDTO);
        serviceDTO.setProducts(service.getProducts());
        return ResponseEntity.ok(serviceDTO);
    }

    @GetMapping("/email/")
    public ResponseEntity<ServiceModel> getServiceByOwner(@RequestParam("owner") String owner, @RequestHeader("Authorization") String token) {
        var user = userClient.validateToken(token);

        if (!user.getAuthorities().contains(new Authority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var service = planService.findByOwner(owner);
        if (service == null) return ResponseEntity.ok(new ServiceModel());

        return ResponseEntity.ok(service);
    }

    @GetMapping("/plans")
    public ResponseEntity<Page<ServiceModel>> getPageableServices(@RequestParam("page") int page, @RequestHeader("Authorization") String token) {
        var user = userClient.validateToken(token);

        if (!user.getAuthorities().contains(new Authority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(planService.getPageableServices(PageRequest.of(page, 20)));
    }

    @GetMapping("/discord")
    public ResponseEntity<ServiceDTO> getServiceByDiscordId(@RequestParam(value = "id") String id) {
        var service = planService.findByDiscordId(id);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        var serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(service, serviceDTO);
        serviceDTO.setProducts(service.getProducts());
        return ResponseEntity.ok(serviceDTO);
    }
}