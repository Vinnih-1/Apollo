package com.apollo.microservice.payment.controllers;

import com.apollo.microservice.payment.dto.CouponDTO;
import com.apollo.microservice.payment.models.CouponModel;
import com.apollo.microservice.payment.repositories.CouponRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @PostMapping("/create")
    public ResponseEntity<CouponModel> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, couponDTO.expirateDays());

        var coupon = CouponModel.builder()
                .name(couponDTO.name())
                .discount(couponDTO.discount())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .isEnabled(true)
                .build();

        couponRepository.saveAndFlush(coupon);

        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/disable")
    public ResponseEntity<CouponModel> disableCoupon(@RequestBody CouponDTO couponDTO) {
        var coupon = couponRepository.findByName(couponDTO.name()).orElse(null);

        if (coupon == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este cupom não foi encontrado!").build();

        coupon.setEnabled(false);
        couponRepository.saveAndFlush(coupon);

        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/enable")
    public ResponseEntity<CouponModel> enableCoupon(@RequestBody CouponDTO couponDTO) {
        var coupon = couponRepository.findByName(couponDTO.name()).orElse(null);

        if (coupon == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este cupom não foi encontrado!").build();

        coupon.setEnabled(true);
        couponRepository.saveAndFlush(coupon);

        return ResponseEntity.ok(coupon);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCoupon(@RequestBody CouponDTO couponDTO) {
        var coupon = couponRepository.findByName(couponDTO.name()).orElse(null);

        if (coupon == null)
            return ResponseEntity.badRequest().header("Error-Message", "Este cupom não foi encontrado!").build();

        couponRepository.delete(coupon);

        return ResponseEntity.ok().build();
    }
}