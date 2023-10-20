package com.apollo.microservice.payment.controllers;

import com.apollo.microservice.payment.dto.NotificationDTO;
import com.apollo.microservice.payment.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/notification")
    public ResponseEntity<Void> paymentNotification(@RequestParam("cliente") String client, @RequestBody NotificationDTO notificationDTO) {
        System.out.println("External Reference: " + client);
        System.out.println(notificationDTO);
        if (notificationDTO.action() == null) return ResponseEntity.ok().build();

        if (notificationDTO.action().equals("payment.updated"))
            paymentService.updatePaymentStatus(client, notificationDTO.data().id());

        return ResponseEntity.ok().build();
    }
}