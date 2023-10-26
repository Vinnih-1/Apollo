package com.microservice.discord.services.discord.listeners.modallisteners.impl;

import com.microservice.discord.dtos.AuthorizeDTO;
import com.microservice.discord.producers.AuthorizeServiceProducer;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class AuthorizeModalListener extends BaseModalListener {

    private final AuthorizeServiceProducer authorizeServiceProducer;

    public AuthorizeModalListener(AuthorizeServiceProducer authorizeServiceProducer) {
        super(
                "modal.auth",
                "Autorização",
                "Modal para link de autorização do Mercado Pago",
                Permission.ADMINISTRATOR
        );
        this.authorizeServiceProducer = authorizeServiceProducer;
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        var serviceKey = event.getValue("service_key").getAsString();
        var serviceId = event.getValue("service_id").getAsString();
        var authorizeDto = new AuthorizeDTO(
                serviceId,
                serviceKey,
                event.getGuild().getId() + "_" + event.getChannel().getId(),
                "",
                ""
        );
        authorizeServiceProducer.publishAuthorizeRequest(authorizeDto);
        event.reply("Estamos gerando o link de autorização...").queue();
    }
}
