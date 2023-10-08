package com.apollo.microservice.payment.repositories;

import com.apollo.microservice.payment.models.ServicePaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePaymentRepository extends JpaRepository<ServicePaymentModel, String> {
}
