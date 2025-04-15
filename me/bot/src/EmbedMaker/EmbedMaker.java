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
        Role helper = event.getGuild().getRoleById("1244410454458110092");
        
        if (event.getChannel().getId().equals("1340330270787633223")) {
            if (content.equalsIgnoreCase("!embed")) {
                Color minhacor = new Color(255, 255, 255);
                Guild guild = event.getGuild();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("📜 Candidaturas para Helper Abertas!");
                embed.setDescription(String.format("""
        Você gostaria de contribuir com a comunidade e ajudar a manter o servidor organizado? Agora você pode se candidatar para a posição de **Helper**!

        <:StarQuestionMark:1340348217589764116> **O que é um %s?\n**  
        Um Helper é um **ajudante**, ou seja, alguém que auxilia no servidor **sem os mesmos poderes de um Staff**.  
        As principais responsabilidades incluem:  

        <a:dot_pink:1292697945350865038> **Receber e dar boas-vindas aos novos membros**, ajudando na integração.  
        <a:dot_pink:1292697945350865038> Ajudar membros com dúvidas sobre o servidor.  
        <a:dot_pink:1292697945350865038> Reportar situações inadequadas para a Staff.  
        <a:dot_pink:1292697945350865038> Monitorar o servidor **visualmente**, sem ferramentas administrativas.  
        <a:dot_pink:1292697945350865038> Buscar parcerias e fortalecer a comunidade.  

        <:ec602eee5ed74d15abcef20591517b2d:1340350203475267636> **Importante:\n**  
        A posição de **Helper** não oferece permissões avançadas. Caso precise agir em alguma situação, o correto é **notificar um superior**.  

        <a:PixelPencil1:1340350444937150525> **Como se candidatar?\n**  
        Preencha o formulário abaixo e aguarde a análise da equipe!\n  
        [📋 Formulário de Aplicação](https://docs.google.com/forms/d/e/1FAIpQLScAIdqA58peF8_LYHOw1gk42Y0AkZOe27wpEb2R99t0uMqvpA/viewform?usp=sharing)  

        <a:light_bulb:1340350757370593374> Se tiver dúvidas, entre em contato com a equipe de Staff. Boa sorte!
        """, helper.getAsMention()));

                embed.setColor(Color.WHITE);
                embed.setImage("https://imgur.com/dffdQ8L.gif");
                embed.setFooter("Equipe de Moderação • Sua ajuda faz a diferença!");

                TextChannel textChannel = event.getGuild().getTextChannelById("1340330270787633223");

                if (textChannel != null) {
                    textChannel.sendMessageEmbeds(embed.build()).queue();
                } else {
                    System.out.println("Canal não encontrado.");
                }
            }
        }
    }
}
