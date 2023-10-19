package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.repositories.CouponRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@RequestMapping("service/coupon")
public class CouponController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CouponRepository couponRepository;

    @PostMapping("/create")
    public ResponseEntity<CouponModel> createServiceCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        var service = serviceRepository.findById(couponDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, couponDTO.expirateDays());

        var coupon = CouponModel.builder()
                .name(couponDTO.name())
                .discount(couponDTO.discount())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .serviceId(couponDTO.serviceId())
                .build();

        serviceRepository.saveAndFlush(service);
        couponRepository.saveAndFlush(coupon);

        return ResponseEntity.status(204).body(coupon);
    }

    @PutMapping("/enable")
    public ResponseEntity<CouponModel> enableServiceCoupon(@RequestBody CouponDTO couponDTO) {
        var service = serviceRepository.findById(couponDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        var coupon = couponRepository.findById(couponDTO.couponId()).orElse(null);

        if (coupon == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este cupom não foi encontrado!").build();

        coupon.setEnabled(true);

        serviceRepository.saveAndFlush(service);
        couponRepository.saveAndFlush(coupon);

        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/disable")
    public ResponseEntity<CouponModel> disableServiceCoupon(@RequestBody CouponDTO couponDTO) {
        var service = serviceRepository.findById(couponDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        var coupon = couponRepository.findById(couponDTO.couponId()).orElse(null);

        if (coupon == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este cupom não foi encontrado!").build();

        coupon.setEnabled(false);

        serviceRepository.saveAndFlush(service);
        couponRepository.saveAndFlush(coupon);

        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteServiceCoupon(@RequestBody CouponDTO couponDTO) {
        var service = serviceRepository.findById(couponDTO.serviceId()).orElse(null);

        if (service == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este serviço não foi encontrado!").build();

        var coupon = couponRepository.findById(couponDTO.couponId()).orElse(null);

        if (coupon == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este cupom não foi encontrado!").build();

        serviceRepository.saveAndFlush(service);
        couponRepository.delete(coupon);

        return ResponseEntity.status(204).build();
    }
}