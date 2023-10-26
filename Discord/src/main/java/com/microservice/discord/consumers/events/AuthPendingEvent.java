package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.AuthStatus;
import com.microservice.discord.models.AuthorizeModel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class AuthPendingEvent extends BaseAuthEvent {

    public AuthPendingEvent() {
        super(AuthStatus.AUTH_PENDING);
    }

    @Override
    public void execute(Guild guild, TextChannel textChannel, AuthorizeModel authorizeModel) {
        textChannel.sendMessage(authorizeModel.getAuthorizeUrl()).queue();
    }
}
