package com.microservice.discord.consumers.events;

import com.microservice.discord.consumers.events.BaseAuthEvent;
import com.microservice.discord.enums.AuthStatus;
import com.microservice.discord.messages.AuthorizeMessages;
import com.microservice.discord.models.AuthorizeModel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class AuthSuccessEvent extends BaseAuthEvent {

    public AuthSuccessEvent() {
        super(AuthStatus.AUTH_SUCCESS);
    }

    @Override
    public void execute(Guild guild, TextChannel textChannel, AuthorizeModel authorizeModel) {
        textChannel.sendMessageEmbeds(
                AuthorizeMessages.AUTHORIZATION_SUCCESSFUL(authorizeModel.getServiceId()).build()
        ).queue();
    }
}
