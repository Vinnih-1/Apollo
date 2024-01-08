package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.PaymentStatus;
import com.microservice.discord.models.PaymentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;

@Getter
@AllArgsConstructor
public abstract class BasePaymentEvent {

    private PaymentStatus paymentStatus;

    public abstract void execute(Guild guild, PaymentModel paymentModel);
}