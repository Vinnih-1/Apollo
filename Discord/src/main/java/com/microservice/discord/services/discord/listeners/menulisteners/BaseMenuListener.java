package com.microservice.discord.services.discord.listeners.menulisteners;

import com.microservice.discord.services.discord.listeners.BaseListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

@Getter
@AllArgsConstructor
public abstract class BaseMenuListener extends BaseListener<StringSelectInteractionEvent> {

    private String id;

    private String name;

    private String description;

    private Permission permission;

    public abstract void execute(StringSelectInteractionEvent event);
}