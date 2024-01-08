package com.apollo.microservice.service.producers;

import com.apollo.microservice.service.dtos.ServiceDTO;
import com.apollo.microservice.service.models.ServiceModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentAuthorizeProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishPaymentAuthorizeResponse(ServiceModel serviceModel) {
        var serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(serviceModel, serviceDTO);
        rabbitTemplate.convertAndSend("", "authorizer.discord", serviceDTO);
    }
}