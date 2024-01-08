package com.microservice.discord.services.discord.listeners.modallisteners.impl;

import com.microservice.discord.dtos.CouponDTO;
import com.microservice.discord.dtos.PaymentDTO;
import com.microservice.discord.dtos.ProductDTO;
import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.producers.ServicePaymentProducer;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class PaymentModalImpl extends BaseModalListener {

    @Autowired
    private ServicePaymentProducer servicePaymentProducer;

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
        var coupon = event.getValue("coupon").getAsString();
        if (!pattern.matcher(email).matches()) {
            event.deferReply(true).addEmbeds(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setDescription("Você precisa digitar um email válido!")
                    .build()).queue();
            return;
        }
        var paymentDTO = PaymentDTO.builder()
                .payer(email)
                .chatId(event.getChannelId())
                .paymentStatus(PaymentStatus.PENDING)
                .product(ProductDTO.builder()
                        .id(Long.parseLong(values.split("_")[0]))
                        .serviceId(values.split("_")[1])
                        .build())
                .coupon(CouponDTO.builder()
                        .name(coupon.toUpperCase())
                        .build())
                .build();
        servicePaymentProducer.publishCreatePaymentMessage(paymentDTO);
        event.deferReply().closeResources().queue();
    }
}
