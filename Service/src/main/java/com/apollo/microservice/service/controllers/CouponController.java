package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.clients.UserClient;
import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.services.PlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("service/coupon")
public class CouponController {

    @Autowired
    private PlanService planService;

    @Autowired
    private UserClient userClient;

    @GetMapping("/")
    public ResponseEntity<List<CouponModel>> getCouponsFromService(@RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        return ResponseEntity.ok(planService.getCouponsFromService(planService.findByOwner(user.getEmail()).getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<CouponModel> createNewCoupon(@Valid @RequestBody CouponDTO couponDTO, @RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        var service = planService.findByOwner(user.getEmail());
        if (service == null) {
            return ResponseEntity.badRequest().build();
        }
        couponDTO.setServiceId(service.getId());
        return ResponseEntity.ok(planService.createNewCoupon(couponDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCoupon(@RequestParam("id") long id, @RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        var service = planService.findByOwner(user.getEmail());
        var coupon = planService.findByCouponId(id);
        if (coupon == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!coupon.getServiceId().equals(service.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        planService.deleteCouponById(id);
        return ResponseEntity.ok().build();
    }
}