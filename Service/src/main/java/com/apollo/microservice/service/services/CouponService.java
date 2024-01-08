package com.apollo.microservice.service.services;

import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public CouponModel createNewCoupon(CouponDTO couponDTO, ServiceModel serviceModel) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, couponDTO.getExpirateDays());
        var coupon = CouponModel.builder()
                .name(couponDTO.getName())
                .usage(0)
                .discount(couponDTO.getDiscount())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .service(serviceModel)
                .maxUsage(couponDTO.getMaxUsage() != null ? couponDTO.getMaxUsage() : -1)
                .isExpired(false)
                .build();

        couponRepository.saveAndFlush(coupon);
        return coupon;
    }

    public CouponModel findByCouponId(long id) {
        return couponRepository.findById(id).orElse(null);
    }

    public void deleteCouponById(long id) {
        couponRepository.deleteById(id);
    }
}
