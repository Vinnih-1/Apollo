package com.apollo.microservice.service.producers;

import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.dtos.PaymentDTO;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.repositories.CouponRepository;
import com.apollo.microservice.service.repositories.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value(value = "${broker.queue.payment.name}")
    private String routingKey;

    public void publishCreatePaymentMessage(PaymentModel paymentModel) {
        var product = productRepository.findById(paymentModel.getProductId()).orElse(null);

        if (product == null) return;

        var paymentDTO = new PaymentDTO(
                paymentModel.getId(),
                paymentModel.getPayer(),
                product.getServiceId(),
                paymentModel.getCoupon() != null ? paymentModel.getCoupon() : null,
                paymentModel.getPaymentStatus(),
                paymentModel.getPaymentIntent(),
                product.getPrice(),
                paymentModel.getProductId()
                );

        rabbitTemplate.convertAndSend("", routingKey, paymentDTO);
    }
}
