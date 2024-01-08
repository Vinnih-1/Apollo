package com.microservice.discord.services.discord.listeners.slashcommands.impl;

import com.microservice.discord.clients.ServiceClient;
import com.microservice.discord.configs.DotEnv;
import com.microservice.discord.messages.AuthorizeMessages;
import com.microservice.discord.messages.ProductMessages;
import com.microservice.discord.services.discord.listeners.slashcommands.BaseSlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ProductMenuSlashCommandImpl extends BaseSlashCommand {

    @Autowired
    private ServiceClient serviceClient;

    @Autowired
    private DotEnv dotEnv;

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
        event.deferReply(true).closeResources().queue();
        try {
            var service = serviceClient.findServiceByDiscordId(event.getGuild().getId(), dotEnv.get("REQUEST_BEARER_TOKEN"));
            var products = service.getProducts();

            if (products.isEmpty()) {
                event.getMessageChannel().sendMessageEmbeds(ProductMessages.EMPTY_PRODUCT_LIST().build()).queue();
                return;
            }
            var menu = StringSelectMenu.create("menu.products");

            // value contains the product id, service id and price of the product separated by "_"
            // 352_3dd23186-6fe0-4b49-a558-a0adcc12b385_15.0
            products.forEach(product -> {
                var values = product.getId() + "_" + service.getId() + "_" + product.getPrice();
                menu.addOption(
                        "R$ " + String.valueOf(product.getPrice()).replace(".", ",") + " - " + product.getName(),
                        values,
                        product.getDescription() == null ? "Produto sem descrição" : product.getDescription(),
                        Emoji.fromUnicode("U+1F4C1"));
            });

            event.getMessageChannel().sendMessageEmbeds(ProductMessages.PRODUCT_MENU_EMBED(service).build())
                    .addActionRow(menu.build()).queue();
        } catch (Exception e) {
            event.getMessageChannel().sendMessageEmbeds(AuthorizeMessages.UNAUTHORIZED().build())
                    .queue();
        }
    }
}
