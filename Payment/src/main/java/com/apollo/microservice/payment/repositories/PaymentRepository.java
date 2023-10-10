package com.apollo.microservice.payment.repositories;

import com.apollo.microservice.payment.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

    Optional<PaymentModel> findByPayer(String payer);
}
