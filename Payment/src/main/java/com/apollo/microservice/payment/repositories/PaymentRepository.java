package com.apollo.microservice.payment.repositories;

import com.apollo.microservice.payment.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {

    Optional<PaymentModel> findByPayer(String payer);

    Optional<PaymentModel> findByExternalReference(String externalReference);

    @Query("SELECT p FROM PaymentModel p WHERE p.expirateAt < ?1")
    List<PaymentModel> findExpiredPayments(Calendar currentDate);
}