package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.AuthStatus;
import com.microservice.discord.messages.AuthorizeMessages;
import com.microservice.discord.models.AuthorizeModel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class AuthExpiredEvent extends BaseAuthEvent {

    public AuthExpiredEvent() {
        super(AuthStatus.AUTH_EXPIRED);
    }

    @Override
    public void execute(Guild guild, TextChannel textChannel, AuthorizeModel authorizeModel) {
        textChannel.sendMessageEmbeds(
                AuthorizeMessages.AUTHORIZATION_EXPIRED(authorizeModel.getServiceId()).build()
        ).queue();
    }
}
