package com.microservice.discord.services.discord.listeners.slashcommands.impl;

import com.microservice.discord.services.discord.listeners.slashcommands.BaseSlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AuthorizeSlashCommandImpl extends BaseSlashCommand {

    public AuthorizeSlashCommandImpl() {
        super(
                "slash.auth",
                "autorizar",
                "Obtenha o link de autorização do Mercado Pago",
                Permission.ADMINISTRATOR
        );
    }

    /**
     * Cria o Modal para receber o ID do serviço
     * junto da Service Key do usuário.
     *
     * @param event
     * @see com.microservice.discord.services.discord.listeners.modallisteners.impl.AuthorizeModalImpl
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        var modal = Modal.create("modal.auth", "Autorização");

        modal.addActionRow(TextInput.create("service_id", "ID do Serviço", TextInputStyle.SHORT)
                .setRequired(true)
                .setPlaceholder("XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX")
                .setMaxLength(36)
                .setMinLength(36)
                .build());
        modal.addActionRow(TextInput.create("service_key", "Chave do Serviço", TextInputStyle.SHORT)
                .setRequired(true)
                .setPlaceholder("XXXXXXXX")
                .setMaxLength(8)
                .setMinLength(8)
                .build());

        event.replyModal(modal.build()).queue();
    }
}
