package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.CouponModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponModel, Long> {

    Optional<List<CouponModel>> findCouponsByServiceId(String serviceId);
}
