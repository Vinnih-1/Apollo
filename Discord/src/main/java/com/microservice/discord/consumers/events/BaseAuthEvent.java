package com.microservice.discord.consumers.events;

import com.microservice.discord.enums.AuthStatus;
import com.microservice.discord.models.AuthorizeModel;
import com.microservice.discord.services.discord.DiscordService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@Getter
@AllArgsConstructor
public abstract class BaseAuthEvent {

    private AuthStatus authStatus;

    public abstract void execute(Guild guild, TextChannel textChannel, AuthorizeModel authorizeModel);
}