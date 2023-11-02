package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.models.PaymentModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;

public class PaymentExpiredEvent extends BasePaymentEvent {

    public PaymentExpiredEvent() {
        super(PaymentStatus.EXPIRED);
    }

    @Override
    public void execute(Guild guild, PaymentModel paymentModel) {
        var channel = guild.getTextChannelById(paymentModel.getChatId());

        channel.getIterableHistory().stream()
                .filter(message -> message.getAuthor().isBot())
                .filter(message -> !message.getAttachments().isEmpty())
                .forEach(message -> {
                    message.getAttachments().stream()
                            .filter(attachment -> attachment.getFileName().equals(String.format("qrcode_%s.png", paymentModel.getExternalReference())))
                            .findFirst()
                            .ifPresent(attachment -> message.delete().queue());
                });
        channel.sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("Este pagamento acaba de ser expirado.")
                        .setFooter("Referência externa: " + paymentModel.getExternalReference())
                        .build())
                .queue();
    }
}