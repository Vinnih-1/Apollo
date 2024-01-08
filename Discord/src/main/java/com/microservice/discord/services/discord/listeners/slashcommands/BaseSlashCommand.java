package com.microservice.discord.services.discord.listeners.slashcommands;

import com.microservice.discord.services.discord.listeners.BaseListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Component
@Scope("prototype")
@AllArgsConstructor
@EqualsAndHashCode(of = "name", callSuper = false)
public abstract class BaseSlashCommand extends BaseListener<SlashCommandInteractionEvent> {

    private String id;

    private String name;

    private String description;

    private Permission permission;

    public BaseSlashCommand(String id) {
        super();
    }

    @Override
    public abstract void execute(SlashCommandInteractionEvent event);
}
