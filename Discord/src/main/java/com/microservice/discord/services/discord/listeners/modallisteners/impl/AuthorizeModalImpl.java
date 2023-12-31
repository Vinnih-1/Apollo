package com.microservice.discord.services.discord.listeners.modallisteners.impl;

import com.microservice.discord.clients.ServiceClient;
import com.microservice.discord.messages.AuthorizeMessages;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
@Scope("prototype")
public class AuthorizeModalImpl extends BaseModalListener {

    @Autowired
    private ServiceClient serviceClient;

    public AuthorizeModalImpl() {
        super(
                "modal.auth",
                "Autorização",
                "Modal para link de autorização do Mercado Pago",
                Permission.ADMINISTRATOR
        );
    }

    /**
     * Obtém o ID do serviço e a senha do serviço
     * para poder receber o link de autorização do
     * Mercado Pago.
     * <p>
     * Após isso, cria o objeto AuthorizationDTO com
     * todos os dados necessários para fazer o request
     * e obter o link de autorização.
     *
     * @param event
     */
    @Override
    public void execute(ModalInteractionEvent event) {
        var serviceKey = event.getValue("service_key").getAsString();
        var serviceId = event.getValue("service_id").getAsString();

        event.getGuild().createCategory("Categoria de Serviços")
                .addPermissionOverride(event.getGuild().getPublicRole(),
                        null,
                        List.of(Permission.VIEW_CHANNEL))
                .queue(category -> {
                    event.deferReply().closeResources().queue();
                    var authorizationLink = serviceClient.authorizationRequest(
                            serviceId,
                            serviceKey,
                            event.getGuild().getId(),
                            category.getId(),
                            event.getChannelId()
                    );
                    event.getChannel().sendMessageEmbeds(
                            AuthorizeMessages.AUTHORIZATION_LINK(authorizationLink,
                                            event.getMember().getUser().getName())
                                    .build()
                    ).queue();
                    
                    if (authorizationLink == null || authorizationLink.isEmpty()) {
                        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                                        .setColor(Color.RED)
                                        .setDescription("Não foi possível vincular este serviço! " +
                                                "Verifique se os campos estão corretos.")
                                        .build())
                                .queue();

                        category.delete().queue();
                    }
                });
    }
}
