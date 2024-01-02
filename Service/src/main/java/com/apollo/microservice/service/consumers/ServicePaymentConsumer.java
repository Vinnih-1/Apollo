package com.apollo.microservice.service.consumers;

import com.apollo.microservice.service.dtos.PaymentDTO;
import com.apollo.microservice.service.models.AuthorizationData;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.producers.ServicePaymentProducer;
import com.apollo.microservice.service.services.PaymentService;
import com.apollo.microservice.service.services.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ServicePaymentConsumer {

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PlanService planService;

    /**
     * Esta fila produz uma nova entidade de pagamento da estaca zero.
     * <p>
     * Utiliza do Service Id para pegar os dados de access token
     * para gerar o QrCode de pagamento e salva o objeto no banco de dados.
     * <p>
     * Ao final de tudo, ele devolve o objeto com os dados completos
     * para a exchange payment
     *
     * @param paymentDTO
     */
    @RabbitListener(queues = "producer.payment")
    public void listenServicePaymentProducerQueue(@Payload PaymentDTO paymentDTO) {
        var product = planService.findProductById(paymentDTO.getProduct().getId());
        var service = planService.findServiceById((paymentDTO.getProduct().getServiceId()));
        var payment = paymentService.generatePaymentData(
                PaymentModel.builder()
                        .payer(paymentDTO.getPayer())
                        .paymentStatus(paymentDTO.getPaymentStatus())
                        .chatId(paymentDTO.getChatId())
                        .product(product)
                        .coupons(paymentDTO.getCoupons())
                        .authorizationData(AuthorizationData.builder()
                                .authorizationChatId(service.getAuthorizationData().getAuthorizationChatId())
                                .discordId(service.getAuthorizationData().getDiscordId())
                                .categoryId(service.getAuthorizationData().getCategoryId())
                                .build())
                        .build(),
                service
        );
        servicePaymentProducer.publishCreatePaymentMessage(payment);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}