package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

    Optional<PaymentModel> findByExternalReference(String externalReference);

    void deleteByExternalReference(String externalReference);
}
