package EmbedMaker;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.Color;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class EmbedMaker extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);

        Message message = event.getMessage();
        String content = message.getContentRaw();
        
        if (event.getChannel().getId().equals("1227975869817950248")) {
            if (content.equalsIgnoreCase("!embed")) {
                Color minhacor = new Color(255, 255, 255);
                Guild guild = event.getGuild();

                EmbedBuilder welcomeEmbed = new EmbedBuilder()
                        .setTitle("Bem-vindo ao canal de parcerias!")
                        .setThumbnail(event.getGuild().getIconUrl())
                        .setDescription("Olá, pessoal. \n\n"
                        + " Nosso servidor está em busca de novas parcerias. Se estiver administrando um servidor"
                        + " e está interessado em fazer um parceria mútua, entre em contato conosco!")
                        .addField("<a:almdecorbowpink:1223409342376706139> **Requisitos:**", "\n\n"
                        + "  <a:Love:1224449007204696237> Mínimo de 100 membros no servidor \n"
                        + "  <a:Love:1224449007204696237> Atividade regular e respeito às regras", false)
                        .addField("<a:almdecorbowpink:1223409342376706139> **Benefícios:**", "\n\n"
                        +"   <a:Love:1224449007204696237> Divulgação mútua nos canais de anúncio \n"
                        + "  <a:Love:1224449007204696237> Integração da Comunidade", false)
                        .addField("<a:almdecorbowpink:1223409342376706139> **Como fazer parceria:**", "\n\n"
                        + "Entre em contato com pessoas de cargo " + guild.getRoleById("1223394386558320680").getAsMention() + "e informe o que é pedido abaixo:\n\n"
                        + "  <a:Love:1224449007204696237> Nome do servidor \n"
                        + "  <a:Love:1224449007204696237> Número de membros \n"
                        + "  <a:Love:1224449007204696237> Breve descrição do servidor\n"
                        + "  <a:Love:1224449007204696237> Link de convite permanente", false)
                        .setImage("https://cdn.discordapp.com/attachments/875131727041937420/877723401219878972/image3-1-4.gif?ex=6629af9e&is=66173a9e&hm=8d572ef3e046609f63e0dedb97052e108a957aba4073e59920e823a546cbef3e&")
                        .setColor(minhacor);

                TextChannel textChannel = event.getGuild().getTextChannelById("1227975869817950248");

                if (textChannel != null) {
                    textChannel.sendMessageEmbeds(welcomeEmbed.build()).queue();
                } else {
                    System.out.println("Canal não encontrado.");
                }
            }
        }
    }
}
