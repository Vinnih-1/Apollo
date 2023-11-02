package com.microservice.discord.services.discord.listeners.buttonlisteners.impl;

import com.microservice.discord.services.discord.listeners.buttonlisteners.BaseButtonListener;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class CancelProductPaymentImpl extends BaseButtonListener {

    public CancelProductPaymentImpl() {
        super(
                "cancel",
                "cancel",
                "Cancela o pagamento do produto",
                null
        );
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        event.getChannel().delete().queue();
    }
}
