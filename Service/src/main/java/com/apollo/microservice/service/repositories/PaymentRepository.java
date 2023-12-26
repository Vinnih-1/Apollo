package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

    Optional<PaymentModel> findByExternalReference(String externalReference);

    Optional<List<PaymentModel>> findAllByServiceId(String serviceId);

    @Query("SELECT p FROM PaymentModel p WHERE p.serviceId = :serviceId AND p.paymentStatus = :paymentStatus")
    Optional<List<PaymentModel>> findAllFilteredByServiceId(String serviceId, PaymentStatus paymentStatus);

    void deleteByExternalReference(String externalReference);
}
