package com.microservice.discord.services.discord.listeners.buttonlisteners;

import com.microservice.discord.services.discord.listeners.BaseListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

@Getter
@AllArgsConstructor
public abstract class BaseButtonListener extends BaseListener<ButtonInteractionEvent> {

    private String id;

    private String name;

    private String description;

    private Permission permission;

    public abstract void execute(ButtonInteractionEvent event);
}