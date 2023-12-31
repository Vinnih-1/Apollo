package com.microservice.discord.consumers.events;

import com.microservice.discord.dtos.ServiceDTO;
import com.microservice.discord.enums.AuthStatus;
import com.microservice.discord.messages.AuthorizeMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class AuthSuccessEvent extends BaseAuthEvent {

    public AuthSuccessEvent() {
        super(AuthStatus.AUTH_SUCCESS);
    }

    /**
     * Envia a mensagem de autorização realizada com êxito
     * no chat em que o comando /autorizar foi executado.
     *
     * @param guild
     * @param textChannel
     * @param serviceDTO
     */
    @Override
    public void execute(Guild guild, TextChannel textChannel, ServiceDTO serviceDTO) {
        textChannel.sendMessageEmbeds(
                AuthorizeMessages.AUTHORIZATION_SUCCESSFUL(serviceDTO.getId()).build()
        ).queue();
    }
}
