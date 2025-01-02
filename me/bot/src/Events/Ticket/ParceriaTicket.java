package Events.Ticket;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import java.awt.Color;
import java.util.Collections;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class ParceriaTicket extends ListenerAdapter 
{

    private static final String OWNER_ROLE_ID = "1223394386558320680";
    private static final String HELPER_ID = "1244410454458110092";
    private static final String TICKET_CHANNEL_ID = "1227975869817950248";
    private static final String PARCERIA_CATEGORY_ID = "1227975047029723247";

     @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // Verificar se a mensagem foi enviada no canal de tickets
        if (event.getChannel().getId().equals(TICKET_CHANNEL_ID)) {
            if (content.equalsIgnoreCase("!openticket")) {
                openTicket(event.getGuild(), event.getMember());
            }
        }
    }

    private void openTicket(Guild guild, Member member) {
        TextChannel ticketChannel = guild.getTextChannelById(TICKET_CHANNEL_ID);
        if (ticketChannel == null) {
            System.out.println("Canal de ticket não encontrado!");
            return;
        }

        // Criar embed para a mensagem de boas-vindas
        Color minhacor = new Color(255, 255, 255);
        EmbedBuilder welcomeEmbed = new EmbedBuilder()
                .setTitle("Bem-vindo ao canal de parcerias!")
                .setThumbnail(guild.getIconUrl())
                .setDescription("Olá, pessoal. \n\n"
                + " Nosso servidor está em busca de novas parcerias. Se estiver administrando um servidor"
                + " e está interessado em fazer um parceria mútua, entre em contato conosco! \n\n"
                + "Se você deseja fazer parceria conosco, por favor, clique no botão abaixo!")
                .setImage("https://pa1.aminoapps.com/7552/c0bed5df916d82abf25aab97ac2d65dd51f18e48r1-540-199_hq.gif")
                .setColor(minhacor);


        // Adicionar botão de abrir ticket
        Button ticketButton = Button.primary("opening_ticket", "Abrir um Ticket agora");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build())
                .setActionRow(ticketButton)
                .queue();
    }
    


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) 
    {
        super.onButtonInteraction(event);
        if (event.getButton().getId().equals("opening_ticket")) {
            Guild guild = event.getGuild();
            Member member = event.getMember();
            Category supportCategory = guild.getCategoryById(PARCERIA_CATEGORY_ID);

            // Criar canal de ticket
            TextChannel ticketChannel = guild.createTextChannel("parceria-" + member.getUser().getName())
                    .setParent(supportCategory)
                    .setTopic("Ticket for " + member.getUser().getName())
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())  // Allow specific permissions to member
                    .addPermissionOverride(guild.getRoleById(OWNER_ROLE_ID), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())  // Allow specific permissions to staff role
                    .addPermissionOverride(guild.getRoleById(HELPER_ID), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))  // Nega permissões para o everyone
                    .complete();

            event.reply("Canal criado em: **" + ticketChannel.getAsMention() + "**!").setEphemeral(true).queue();

            ticketChannel.sendMessage(member.getAsMention() + guild.getRoleById(OWNER_ROLE_ID).getAsMention() + guild.getRoleById(HELPER_ID).getAsMention()).queue();
            Color minhacor = new Color(255, 255, 255);
            EmbedBuilder welcomeEmbed = new EmbedBuilder()
            .setTitle("Bem-vindo ao canal de parcerias!")
            .setThumbnail(event.getGuild().getIconUrl())
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
            
           

            Button ticketButton = Button.primary("closing_ticket", "Fechar o Ticket agora");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build()).setActionRow(ticketButton).queue();

    

        }

        if (event.getComponentId().equals("closing_ticket"))
        {
            if (event.getChannel() instanceof TextChannel) {
                TextChannel ticketChannel = (TextChannel) event.getChannel();

                for (int i = 3; i > 0; i--) 
                {
                    ticketChannel.sendMessage("Ticket fechando em " + i + " segundos...").queue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }

                ticketChannel.sendMessage("Ticket fechando...").queue();
                closeTicket(ticketChannel);
            }
        }
    }

    private void closeTicket(TextChannel ticketChannel) 
    {
        // Fechar o ticket imediatamente
            ticketChannel.delete().queueAfter(0, TimeUnit.SECONDS);
        
    }

    
    
    
}
