package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.PaymentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

    Optional<PaymentModel> findByExternalReference(String externalReference);

    @Query("SELECT p FROM PaymentModel p WHERE p.product.service.id = :serviceId")
    Page<PaymentModel> findAllByServiceId(Pageable pageable, String serviceId);

    void deleteByExternalReference(String externalReference);
}
