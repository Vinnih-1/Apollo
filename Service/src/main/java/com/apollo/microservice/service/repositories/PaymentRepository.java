package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {
}
