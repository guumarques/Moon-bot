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

public class Ticket extends ListenerAdapter 
{

    private static final String STAFF_ROLE_ID = "1223852657244897361";
    private static final String TICKET_CHANNEL_ID = "1223416214110605374";
    private static final String SUPPORT_CATEGORY_ID = "1223415954550034433";

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
                .setTitle("Bem-vindo ao canal de denúncias!")
                .setThumbnail(guild.getIconUrl())
                .setDescription("Se você identificou alguma violação de regras, você está no lugar certo.\n\nしAntes de mais nada, por favor, verifique o canal <#1223394240319586335> para se certificar da sua denúncia.")
                .setImage("https://cdn.discordapp.com/attachments/875131727041937420/888663341680189450/image0-1-1.gif?ex=661a1d3b&is=6607a83b&hm=ee4ed0541021a372af7cad519c071597190547670b23e88ba0d8815d486526e3&")
                .setColor(minhacor);


        // Adicionar botão de abrir ticket
        Button ticketButton = Button.primary("open_ticket", "Abrir Ticket");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build())
                .setActionRow(ticketButton)
                .queue();
    }
    


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) 
    {
        super.onButtonInteraction(event);
        if (event.getButton().getId().equals("open_ticket")) {
            Guild guild = event.getGuild();
            Member member = event.getMember();
            Category supportCategory = guild.getCategoryById(SUPPORT_CATEGORY_ID);

            // Criar canal de ticket
            TextChannel ticketChannel = guild.createTextChannel("ticket-" + member.getUser().getName())
                    .setParent(supportCategory)
                    .setTopic("Ticket for " + member.getUser().getName())
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())  // Allow specific permissions to member
                    .addPermissionOverride(guild.getRoleById(STAFF_ROLE_ID), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())  // Allow specific permissions to staff role
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))  // Nega permissões para o everyone
                    .complete();

            event.reply("Canal criado em: **" + ticketChannel.getAsMention() + "**!").setEphemeral(true).queue();

            ticketChannel.sendMessage(member.getAsMention() + guild.getRoleById(STAFF_ROLE_ID).getAsMention()).queue();
            Color minhacor = new Color(255, 255, 255);
            EmbedBuilder welcomeEmbed = new EmbedBuilder()
                .setTitle("Bem-vindo ao canal de denúncias!")
                .setThumbnail(guild.getIconUrl())
                .setDescription("**" + member.getUser().getGlobalName() + "**" + ", um staff irá atendê-lo em alguns instantes."
                + " Por favor, aguarde! \n\n し**Enquanto isso, por favor, informe sua denúncia. Siga as**"
                + " **recomendações abaixo.**")
                .addField("<a:9174heartarrow:1225889006995112048> Violação por escrito:", "Caso tenha acontecido alguma quebra de regra em um chat "
                + "público do servidor, favor enviar uma captura de tela e, posteriormente, um staff irá analisar.", false)
                .addField("<a:9174heartarrow:1225889006995112048> Violação por chamada de voz:", "Caso tenha acontecido alguma quebra de regra em uma"
                + " chamada de voz do servidor, favor anexar uma gravação de vídeo referente ao acontecimento."
                + " Caso não seja possível, favor chamar um staff.\n\n**Observação**: *É estritamente proibido "
                + "fazer denúncias falsas ou de brincadeira. Qualquer tentativa de denúncia falsa, seja por "
                + "mal-entendido ou intenção deliberada, resultará em punições. A denúncia deve ser feita "
                + "com seriedade, considerando suas consequências e baseada em informações precisas e "
                + "verificáveis. O descumprimento dessa regra será tratado com rigor, visando preservar a "
                + "integridade e a credibilidade do sistema de denúncias*.", false)
                .setImage("https://cdn.discordapp.com/attachments/875131727041937420/888663341680189450/image0-1-1.gif?ex=661a1d3b&is=6607a83b&hm=ee4ed0541021a372af7cad519c071597190547670b23e88ba0d8815d486526e3&")
                .setColor(minhacor);
            
           

            Button ticketButton = Button.primary("close_ticket", "Fechar Ticket");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build()).setActionRow(ticketButton).queue();

    

        }

        if (event.getComponentId().equals("close_ticket"))
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
