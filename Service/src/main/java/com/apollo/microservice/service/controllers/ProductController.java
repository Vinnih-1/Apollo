package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.clients.ServiceClient;
import com.apollo.microservice.service.clients.UserClient;
import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.producers.ServicePaymentProducer;
import com.apollo.microservice.service.services.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("service/product")
public class ProductController {

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @Autowired
    private PlanService planService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ServiceClient serviceClient;

    @PostMapping("/create")
    public ResponseEntity<ProductModel> createServiceProduct(@Valid @RequestBody ProductDTO productDTO, @RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        var service = planService.findByOwner(user.getEmail());
        if (service.getId() == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(planService.createNewProduct(productDTO, service));
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductModel>> getProductsByService(@RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        var service = planService.findByOwner(user.getEmail());
        return ResponseEntity.ok(service.getProducts());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProductById(@RequestParam("id") long id, @RequestHeader("Authorization") String token) {
        var service = serviceClient.getServiceByToken(token);
        if (service.getProducts() == null) return ResponseEntity.badRequest().build();
        var ownsProduct = service.getProducts().stream().anyMatch(product -> product.getId() == id);
        if (!ownsProduct) return ResponseEntity.badRequest().build();
        planService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }
}