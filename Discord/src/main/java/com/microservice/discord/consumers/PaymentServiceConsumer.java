package com.microservice.discord.consumers;

import com.microservice.discord.consumers.events.BasePaymentEvent;
import com.microservice.discord.consumers.events.PaymentExpiredEvent;
import com.microservice.discord.consumers.events.PaymentPendingEvent;
import com.microservice.discord.consumers.events.PaymentSuccessEvent;
import com.microservice.discord.models.PaymentModel;
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

    /**
     * Obtém a mensagem publicada pelo microsserviço Service,
     * separa pelo status do pagamento e envia a mensagem
     * adequada para cada tipo de status.
     *
     * @param paymentModel
     */
    @RabbitListener(queues = "payment.discord")
    public void listenPaymentQueue(@Payload PaymentModel paymentModel) {
        var guild = discordService.getJda().getGuildById(paymentModel.getAuthorizationData().getDiscordId());
        events.stream()
                .filter(event -> event.getPaymentStatus() == paymentModel.getPaymentStatus())
                .findFirst()
                .ifPresent(event -> event.execute(guild, paymentModel));
    }
}
