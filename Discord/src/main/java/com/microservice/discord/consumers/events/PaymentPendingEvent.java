package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.messages.ProductMessages;
import com.microservice.discord.models.PaymentModel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.Base64;

public class PaymentPendingEvent extends BasePaymentEvent {

    public PaymentPendingEvent() {
        super(PaymentStatus.PENDING);
    }

    @Override
    public void execute(Guild guild, PaymentModel paymentModel) {
        if (paymentModel.getQrcodeBase64() == null) return;

        guild.getTextChannelById(paymentModel.getChatId())
                .sendMessageEmbeds(ProductMessages.PAYMENT_PENDING_EMBED(paymentModel).build())
                .addFiles(FileUpload.fromData(Base64.getDecoder().decode(paymentModel.getQrcodeBase64()),
                        String.format("qrcode_%s.png", paymentModel.getId())))
                .queue();
    }
}