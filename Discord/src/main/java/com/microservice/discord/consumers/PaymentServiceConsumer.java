package com.microservice.discord.consumers;

import com.microservice.discord.consumers.events.BasePaymentEvent;
import com.microservice.discord.consumers.events.PaymentExpiredEvent;
import com.microservice.discord.consumers.events.PaymentPendingEvent;
import com.microservice.discord.consumers.events.PaymentSuccessEvent;
import com.microservice.discord.models.PaymentModel;
import com.microservice.discord.requests.ServiceRequest;
import com.microservice.discord.services.discord.DiscordService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PaymentServiceConsumer {

    private final List<BasePaymentEvent> events = Arrays.asList(
            new PaymentSuccessEvent(),
            new PaymentExpiredEvent(),
            new PaymentPendingEvent()
    );

    @Autowired
    private DiscordService discordService;

    @RabbitListener(queues = "${broker.queue.service.payment}")
    public void listenPaymentQueue(@Payload PaymentModel paymentModel) {
        ServiceRequest.getInstance()
                .retrieveServiceByServiceId(paymentModel.getServiceId(), service -> {
                    var guild = discordService.getJda().getGuildById(service.discordId());

                    events.stream()
                            .filter(event -> event.getPaymentStatus() == paymentModel.getPaymentStatus())
                            .findFirst()
                            .ifPresent(event -> event.execute(guild, paymentModel));
                });
    }
}
