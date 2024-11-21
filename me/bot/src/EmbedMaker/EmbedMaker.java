package EmbedMaker;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
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
        Role boost = event.getGuild().getRoleById("1223395224626270409");
        
        if (event.getChannel().getId().equals("1223401314617393162")) {
            if (content.equalsIgnoreCase("!embed")) {
                Color minhacor = new Color(255, 255, 255);
                Guild guild = event.getGuild();

                EmbedBuilder welcomeEmbed = new EmbedBuilder()
                        .setTitle("<:Booster:1292696909986922576> **Santuário Lunar - Booster** <a:evolving_badge_boost:1292696671410589706>")
                        .setThumbnail(event.getGuild().getIconUrl())
                        .setDescription("<a:Boost:1292697291068801065> Ajude nosso servidor impulsionando e garanta os diversos benefícios abaixo. É muito simples e vantajoso <a:exclamation_animated:1292698505282064464> \n\n"
                        + "<:4114pinkmoon:1228440490727182366> **Benefícios**\n"
                                + " <a:dot_pink:1292697945350865038> Permissão de envio de imagens em outros chat's;\n"
                                + " <a:dot_pink:1292697945350865038> Acesso à tag: " + boost.getAsMention() + ";\n"
                                + " <a:dot_pink:1292697945350865038> Permissão para enviar emojis de outros servidores;\n"
                                + " <a:dot_pink:1292697945350865038> Call exclusiva para boosters;\n"
                                + " <a:dot_pink:1292697945350865038> Emblema de evolução no seu perfil;\n"
                                + " <a:dot_pink:1292697945350865038> Tag's que evoluem com o passar do tempo de booster;\n"
                                + " <a:dot_pink:1292697945350865038> E para finalizar um Bônus de xp.")
                        .setColor(Color.decode("#fccfcc"));

                TextChannel textChannel = event.getGuild().getTextChannelById("1223401314617393162");

                if (textChannel != null) {
                    textChannel.sendMessageEmbeds(welcomeEmbed.build()).queue();
                } else {
                    System.out.println("Canal não encontrado.");
                }
            }
        }
    }
}
