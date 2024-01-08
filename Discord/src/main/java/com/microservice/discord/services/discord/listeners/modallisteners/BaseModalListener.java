package com.microservice.discord.services.discord.listeners.modallisteners;

import com.microservice.discord.services.discord.listeners.BaseListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

@Getter
@AllArgsConstructor
public abstract class BaseModalListener extends BaseListener<ModalInteractionEvent> {

    private String id;

    private String name;

    private String description;

    private Permission permission;

    public abstract void execute(ModalInteractionEvent event);
}