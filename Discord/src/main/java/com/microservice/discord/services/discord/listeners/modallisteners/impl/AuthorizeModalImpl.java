package com.microservice.discord.services.discord.listeners.modallisteners.impl;

import com.microservice.discord.dtos.AuthorizeDTO;
import com.microservice.discord.requests.ServiceRequest;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.List;

public class AuthorizeModalImpl extends BaseModalListener {

    public AuthorizeModalImpl() {
        super(
                "modal.auth",
                "Autorização",
                "Modal para link de autorização do Mercado Pago",
                Permission.ADMINISTRATOR
        );
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        var serviceKey = event.getValue("service_key").getAsString();
        var serviceId = event.getValue("service_id").getAsString();

        event.getGuild().createCategory("Categoria de Serviços")
                .addPermissionOverride(event.getGuild().getPublicRole(),
                        null,
                        List.of(Permission.VIEW_CHANNEL))
                .queue(category -> {
                    var authorizeDto = new AuthorizeDTO(
                            serviceId,
                            serviceKey,
                            event.getGuild().getId(),
                            category.getId(),
                            event.getChannelId(),
                            "",
                            ""
                    );
                    event.deferReply().closeResources().queue();

                    ServiceRequest.getInstance()
                            .authorizeRequest(authorizeDto, onSuccess -> {
                                event.getChannel().sendMessage(onSuccess.getAuthorizeUrl()).queue();
                            });
                });
    }
}
