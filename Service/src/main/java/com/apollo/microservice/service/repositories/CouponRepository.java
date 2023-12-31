package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.CouponModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponModel, Long> {
}
