package com.microservice.discord.services.discord.listeners.slashcommands.impl;

import com.microservice.discord.messages.ProductMessages;
import com.microservice.discord.requests.ServiceRequest;
import com.microservice.discord.services.discord.listeners.slashcommands.BaseSlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class ProductMenuSlashCommandImpl extends BaseSlashCommand {

    public ProductMenuSlashCommandImpl() {
        super(
                "slash.menu",
                "menu",
                "Cria o menu de produtos do serviço",
                Permission.ADMINISTRATOR
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).setContent("Requisitando produtos...").queue();

        ServiceRequest.getInstance()
                .retrieveServiceByDiscordId(event.getGuild().getId(), service -> {
                    var products = service.products();

                    if (products.isEmpty()) {
                        event.getMessageChannel().sendMessageEmbeds(ProductMessages.EMPTY_PRODUCT_LIST().build()).queue();
                        return;
                    }
                    var menu = StringSelectMenu.create("menu.products");

                    // value contains the product id, service id and price of the product separated by "_"
                    // 352_3dd23186-6fe0-4b49-a558-a0adcc12b385_15.0
                    products.forEach(product -> menu.addOption(
                            "R$ " + String.valueOf(product.price()).replace(".", ",") + " - " + product.name(),
                            product.id() + "_" + product.serviceId() + "_" + product.price(),
                            product.description() == null ? "Produto sem descrição" : product.description(),
                            Emoji.fromUnicode("U+1F4C1")));

                    event.getMessageChannel().sendMessageEmbeds(ProductMessages.PRODUCT_MENU_EMBED(service).build())
                            .addActionRow(menu.build()).queue();
                });
    }
}
