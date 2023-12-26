package com.apollo.microservice.payment.services;

import com.apollo.microservice.payment.dtos.PaymentDTO;
import com.apollo.microservice.payment.enums.PaymentIntent;
import com.apollo.microservice.payment.enums.PaymentStatus;
import com.apollo.microservice.payment.models.PaymentModel;
import com.apollo.microservice.payment.producers.PlanPaymentProducer;
import com.apollo.microservice.payment.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PlanPaymentProducer planPaymentProducer;

    public Page<PaymentModel> getPageablePaymentsPlan(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    public List<PaymentDTO> getPendingPaymentPlan(String serviceId) {
        return paymentRepository.findAllByServiceId(serviceId).orElse(null).stream()
                .map(payment -> new PaymentDTO(
                        payment.getId(),
                        payment.getPayer(),
                        payment.getServiceId(),
                        payment.getChatId(),
                        "",
                        payment.getCoupon(),
                        payment.getPaymentStatus(),
                        payment.getPaymentIntent(),
                        payment.getPrice(),
                        payment.getProductId()
                )).toList();
    }

    public PaymentModel forceAuthorizePaymentPlan(String externalReference) {
        var paymentModel = paymentRepository.findByExternalReference(externalReference).orElse(null);

        // checks if payment model exists with this external reference and
        // if this payment model has CREATE_SERVICE intent
        if (paymentModel == null) return null;
        if (paymentModel.getPaymentIntent() != PaymentIntent.CREATE_SERVICE) return null;

        // set the payment status like PAYED, send a message for service-ms
        // to create this service and remove the payment model from repository
        paymentModel.setPaymentStatus(PaymentStatus.PAYED);
        planPaymentProducer.sendPlanPaymentService(paymentModel);
        paymentRepository.delete(paymentModel);

        return paymentModel;
    }
}
