package com.microservice.discord.messages;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class AuthorizeMessages {

    private static final String LOGO_URL = "https://cdn.discordapp.com/attachments/1148878336651698208/1152991830774906920/Apollo-512x512.png";

    public static EmbedBuilder AUTHORIZATION_LINK(String link, String owner) {
        var embed = getEmbedBuilder("Link de Autorização");
        embed.setFooter("Pendente・Clique no link acima para autorizar");

        embed.addField(":mag_right: **__Link de Autorização__**",
                String.format("[Clique Aqui](%s)", link),
                true);

        embed.addField(":bust_in_silhouette: **__Autor do Comando__**",
                owner,
                true);

        embed.setDescription("Não compartilhe este link com ninguém!" +
                " Ao clicar neste link, você autoriza a criação e detecção de pagamentos" +
                "direcionados para a sua conta do **Mercado Pago**.");

        return embed;
    }

    public static EmbedBuilder AUTHORIZATION_SUCCESSFUL(String serviceId) {
        var embed = getEmbedBuilder("Autorizado com Êxito");
        embed.setFooter("Sucesso・Conta vinculada com este Serviço");

        embed.addField(":identification_card: **__ID do Serviço__**",
                serviceId,
                false);

        embed.setDescription("Agora todos os pagamentos que forem criados e pagos," +
                " serão redirecionados para a sua conta do **Mercado Pago**!");

        return embed;
    }

    public static EmbedBuilder AUTHORIZATION_EXPIRED(String serviceId) {
        var embed = getEmbedBuilder("Erro na Autorização");
        embed.setFooter("Falha・Não foi possível vincular sua conta a este Serviço");

        embed.addField(":identification_card: **__ID do Serviço__**",
                serviceId,
                false);

        embed.setDescription("A autorização aparenta ter falhado, pois o link de autorização expirou. " +
                "Por favor, tente novamente...");

        return embed;
    }

    public static EmbedBuilder UNAUTHORIZED() {
        var embed = getEmbedBuilder("Discord não Autorizado");
        embed.setFooter("Falha・Não encontrei nenhum serviço vinculado");

        embed.setDescription("Este discord não está vinculado a nenhum serviço cadastrado! " +
                "Por favor, utilize o comando `/autorizar` para vincular este discord.");

        return embed;
    }

    private static EmbedBuilder getEmbedBuilder(String title) {
        var embed = new EmbedBuilder();
        embed.setColor(Color.GRAY);
        embed.setAuthor(title,
                null,
                LOGO_URL);

        embed.setThumbnail(LOGO_URL);
        return embed;
    }
}