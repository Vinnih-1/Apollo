package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.clients.UserClient;
import com.apollo.microservice.service.dtos.Authority;
import com.apollo.microservice.service.dtos.ServiceDTO;
import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.PaymentModel;
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

import java.util.List;

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
    public ResponseEntity<ServiceModel> getServiceByToken(@RequestHeader("Authorization") String token) {
        var user = userClient.validateToken(token.substring(7));
        if (!user.isValid()) return ResponseEntity.badRequest().build();
        var service = planService.findByOwner(user.getEmail());
        if (service == null) return ResponseEntity.ok(new ServiceModel());
        return ResponseEntity.ok(service);
    }

    @GetMapping("/email/")
    public ResponseEntity<ServiceModel> getServiceByOwner(@RequestParam("owner") String owner, @RequestHeader("Authorization") String token) {
        var user = userClient.validateToken(token.substring(7));

        if (!user.getAuthorities().contains(new Authority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var service = planService.findByOwner(owner);
        if (service == null) return ResponseEntity.ok(new ServiceModel());

        return ResponseEntity.ok(service);
    }

    @GetMapping("/plans")
    public ResponseEntity<Page<ServiceModel>> getPageableServices(@RequestParam("page") int page, @RequestHeader("Authorization") String token) {
        var user = userClient.validateToken(token.substring(7));

        if (!user.getAuthorities().contains(new Authority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(planService.getPageableServices(PageRequest.of(page, 20)));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentModel>> getPaymentsService(@RequestParam("status") PaymentStatus paymentStatus, @RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        return ResponseEntity.ok(planService.getPaymentsFromService(planService.findByOwner(user.getEmail()), paymentStatus));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceByServiceId(@PathVariable(value = "id") String id) {
        var service = planService.findServiceById(id);

        if (service == null)
            return ResponseEntity.badRequest().build();
        /*var serviceDto = new ServiceDTO(
                service.getId(),
                "",
                service.getOwner(),
                service.getDiscordId(),
                service.getCategoryId(),
                null,
                0,
                0,
                service.getProducts(),
                service.getCoupons()
        );*/

        return ResponseEntity.ok().build();
    }

    @GetMapping("/discord")
    public ResponseEntity<ServiceDTO> getServiceByDiscordId(@RequestParam(value = "id") String id) {
        var service = planService.findByDiscordId(id);
        if (service == null) return ResponseEntity.badRequest().build();
        var serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(service, serviceDTO);
        serviceDTO.setProducts(service.getProducts());
        return ResponseEntity.ok(serviceDTO);
    }
}