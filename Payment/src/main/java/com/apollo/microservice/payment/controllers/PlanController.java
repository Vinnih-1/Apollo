package com.apollo.microservice.payment.controllers;

import com.apollo.microservice.payment.clients.ServiceClient;
import com.apollo.microservice.payment.clients.UserClient;
import com.apollo.microservice.payment.dtos.AuthorityDTO;
import com.apollo.microservice.payment.dtos.PaymentDTO;
import com.apollo.microservice.payment.enums.PaymentIntent;
import com.apollo.microservice.payment.enums.PaymentStatus;
import com.apollo.microservice.payment.enums.ServiceType;
import com.apollo.microservice.payment.models.PaymentModel;
import com.apollo.microservice.payment.repositories.PaymentRepository;
import com.apollo.microservice.payment.services.PaymentService;
import com.apollo.microservice.payment.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("payment/plan")
public class PlanController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PlanService planService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ServiceClient serviceClient;

    @GetMapping("authorize")
    public ResponseEntity<PaymentModel> authorizePaymentPlan(@RequestParam("externalReference") String externalReference, @RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        if (!user.getAuthorities().contains(new AuthorityDTO("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(planService.forceAuthorizePaymentPlan(externalReference));
    }

    @GetMapping("pending")
    public ResponseEntity<List<PaymentDTO>> getPendingPayments(@RequestHeader("Authorization") String token) {
        var service = serviceClient.getServiceByToken(token);
        return ResponseEntity.ok(planService.getPendingPaymentPlan(service.id()));
    }

    @GetMapping("payments")
    public ResponseEntity<Page<PaymentModel>> getPageablePaymentPlan(@RequestParam("page") int page, @RequestHeader("Authorization") String token) {
        var user = userClient.userByToken(token);
        if (!user.getAuthorities().contains(new AuthorityDTO("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(planService.getPageablePaymentsPlan(PageRequest.of(page, 20)));
    }

    @GetMapping("professional")
    public ResponseEntity<PaymentModel> generatePaymentPlan(@RequestHeader("Authorization") String token) {
        var calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
        var user = userClient.userByToken(token);
        calendar.add(Calendar.MINUTE, 30);

        if (paymentRepository.findByPayer(user.getEmail()).isPresent()) {
            var payment = paymentRepository.findByPayer(user.getEmail()).get();
            if (payment.getPaymentIntent() == PaymentIntent.CREATE_SERVICE)
                return ResponseEntity.ok(payment);
        }

        var paymentModel = PaymentModel.builder()
                .payer(user.getEmail())
                .paymentStatus(PaymentStatus.PENDING)
                .serviceType(ServiceType.PROFESSIONAL)
                .price(ServiceType.PROFESSIONAL.getPrice())
                .createAt(Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo")))
                .expirateAt(calendar)
                .paymentIntent(PaymentIntent.CREATE_SERVICE)
                .build();

        return ResponseEntity.ok(paymentService.generatePaymentData(paymentModel));
    }
}
