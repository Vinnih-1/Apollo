package com.microservice.discord.services.discord.listeners.buttonlisteners.impl;

import com.microservice.discord.services.discord.listeners.buttonlisteners.BaseButtonListener;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GenerateProductPaymentImpl extends BaseButtonListener {

    public GenerateProductPaymentImpl() {
        super(
                "generate",
                "generate",
                "Gera o QRCode para o pagamento do produto",
                null
        );
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        var value = event.getButton().getId().replace("generate_", "");
        var modal = Modal.create("paymentmodal_" + value, "Preencha os dados");
        System.out.println("Button: " + value);
        modal.addActionRow(TextInput.create("email", "Email", TextInputStyle.SHORT)
                .setPlaceholder("meuemail@gmail.com")
                .build());
        modal.addActionRow(TextInput.create("coupon", "Cupom", TextInputStyle.SHORT)
                .setPlaceholder("NATAL20")
                .setRequired(false)
                .build());

        event.replyModal(modal.build()).queue();
    }
}
