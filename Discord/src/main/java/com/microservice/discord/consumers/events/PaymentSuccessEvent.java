package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.messages.ProductMessages;
import com.microservice.discord.models.PaymentModel;
import net.dv8tion.jda.api.entities.Guild;

public class PaymentSuccessEvent extends BasePaymentEvent {

    public PaymentSuccessEvent() {
        super(PaymentStatus.PAYED);
    }

    @Override
    public void execute(Guild guild, PaymentModel paymentModel) {
        var channel =  guild.getTextChannelById(paymentModel.getChatId());

        channel.sendMessageEmbeds(ProductMessages.PRODUCT_PAYED_EMBED(paymentModel).build())
                .queue();
        channel.getIterableHistory().stream()
                .filter(message -> message.getAuthor().isBot())
                .filter(message -> !message.getAttachments().isEmpty())
                .forEach(message -> {
                    message.getAttachments().stream()
                            .filter(attachment -> attachment.getFileName().equals(String.format("qrcode_%s.png", paymentModel.getExternalReference())))
                            .findFirst()
                            .ifPresent(attachment -> message.delete().queue());
                });
    }
}