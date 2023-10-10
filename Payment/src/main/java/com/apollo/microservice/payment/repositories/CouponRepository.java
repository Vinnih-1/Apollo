package com.apollo.microservice.payment.repositories;

import com.apollo.microservice.payment.models.CouponModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponModel, Long> {

    Optional<CouponModel> findByName(String name);
}
