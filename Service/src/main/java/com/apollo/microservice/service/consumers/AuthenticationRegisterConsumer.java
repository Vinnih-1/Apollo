package com.apollo.microservice.service.consumers;

import com.apollo.microservice.service.dtos.UserDTO;
import com.apollo.microservice.service.services.PlanService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationRegisterConsumer {

    @Autowired
    private PlanService planService;

    @RabbitListener(queues = "authorizer.service")
    public void listenRegisterAccountQueue(@Payload UserDTO userDTO) {
        planService.createNewService(userDTO.getEmail());
    }
}
