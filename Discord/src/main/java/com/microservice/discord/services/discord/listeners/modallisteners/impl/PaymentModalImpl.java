package com.microservice.discord.services.discord.listeners.modallisteners.impl;

import com.microservice.discord.dtos.PaymentDTO;
import com.microservice.discord.enums.PaymentIntent;
import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.requests.ServiceRequest;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.awt.*;
import java.util.regex.Pattern;

public class PaymentModalImpl extends BaseModalListener {

    public PaymentModalImpl() {
        super(
                "paymentmodal",
                "payment",
                "Modal para preenchimento de dados para geração do QRCode",
                null
        );
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        var values = event.getModalId().replace("paymentmodal_", "");
        var pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        var email = event.getValue("email").getAsString();

        if (!pattern.matcher(email).matches()) {
            event.deferReply(true).addEmbeds(new EmbedBuilder()
                            .setColor(Color.RED)
                            .setDescription("Você precisa digitar um email válido!")
                    .build()).queue();
            return;
        }

        var paymentDTO = new PaymentDTO(
                "",
                email,
                event.getGuild().getId(),
                event.getChannelId(),
                event.getValue("coupon").getAsString(),
                PaymentStatus.PENDING,
                PaymentIntent.SELL_PRODUCT,
                Double.parseDouble(values.split("_")[2]),
                Long.parseLong(values.split("_")[0])
        );

        ServiceRequest.getInstance()
                .sendPaymentRequest(paymentDTO, onSuccess -> {});
        event.deferReply().closeResources().queue();
    }
}
