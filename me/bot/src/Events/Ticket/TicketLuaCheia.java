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

public class TicketLuaCheia extends ListenerAdapter
{
    private static final String OWNER_ROLE_ID = "1223394386558320680";
    private static final String TICKET_CHANNEL_LUACHEIA_ID = "1228889783137472583";
    private static final String VIP_CATEGORY_ID = "1228889229816369264";

    String idcargoLuaCheia = "1225963752076083231";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // Verificar se a mensagem foi enviada no canal de tickets
        if (event.getChannel().getId().equals(TICKET_CHANNEL_LUACHEIA_ID)) {
            if (content.equalsIgnoreCase("!openticket")) {
                openTicket(event.getGuild(), event.getMember());
            }
        }
    }

    private void openTicket(Guild guild, Member member) {
        TextChannel ticketChannel = guild.getTextChannelById(TICKET_CHANNEL_LUACHEIA_ID);
        if (ticketChannel == null) {
            System.out.println("Canal de ticket não encontrado!");
            return;
        }

        // Criar embed para a mensagem de boas-vindas
        Color minhacor = new Color(252,238,81,255);
        EmbedBuilder welcomeEmbed = new EmbedBuilder()
                .setAuthor("››>> Vip's Santuário Lunar <<‹‹")
                .setTitle("<:9057moon1:1228440487594164334> " + " Vip Lua Cheia")
                .setDescription("Quais os beneficios desse Vip? Veja logo abaixo! \n")
                .addField("<:yellow_vip_badge:1344080333531910144> Cargos Personalizáveis",
                        "<a:download:1261792222320398456>  Criar e editar **moonvip**\n" +
                        "<a:download:1261792222320398456>  Criar e editar **moonfriend**\n", false)

                .addField("<:yellow_gift1:1344082175045271604> Outros Benefícios",
                        "<a:download:1261792222320398456> Criar sua própria **call** \n" +
                        "<a:download:1261792222320398456> Enviar imagens no **chat geral** \n", false)
                .addField("<:goldenstar:1344095277694455849> Descrição dos Cargos:",
                        "<a:download:1261792222320398456> **moonvip** - Cargo **único**.\n" +
                        "<a:download:1261792222320398456> **moonfriend** - Cargo **para até 8 pessoas**. \n", false)
                .setColor(minhacor)
                .setImage("https://media1.tenor.com/m/hVxzEeY3_asAAAAC/flower-rain.gif")
                .setThumbnail("https://cdn.discordapp.com/attachments/1118914929399955538/1228606934551367753/luacheia.png?ex=662ca85f&is=661a335f&hm=350c56eb8c140859d50a738b97691454e3ea1dff88369208a5c703b7e28189d2&")
                .addField("Cargo : ", guild.getRoleById(idcargoLuaCheia).getAsMention(), false);

        TextChannel textChannel = guild.getTextChannelById(TICKET_CHANNEL_LUACHEIA_ID);

        if (textChannel != null) {
            textChannel.sendMessageEmbeds(welcomeEmbed.build()).queue();
        } else {
            System.out.println("Canal não encontrado.");
        }


        // Adicionar botão de abrir ticket
        Button ticketButton = Button.primary("open_luacheia_ticket", "Comprar agora");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build())
                .setActionRow(ticketButton)
                .queue();
    }



    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        super.onButtonInteraction(event);
        if (event.getButton().getId().equals("open_luacheia_ticket")) {
            Guild guild = event.getGuild();
            Member member = event.getMember();
            Category supportCategory = guild.getCategoryById(VIP_CATEGORY_ID);

            // Criar canal de ticket
            TextChannel ticketChannel = guild.createTextChannel("lua-cheia-vip-" + member.getUser().getName())
                    .setParent(supportCategory)
                    .setTopic("Ticket for " + member.getUser().getName())
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())  // Allow specific permissions to member
                    .addPermissionOverride(guild.getRoleById(OWNER_ROLE_ID), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), Collections.emptyList())  // Allow specific permissions to staff role
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))  // Nega permissões para o everyone
                    .complete();

            event.reply("Canal criado em: **" + ticketChannel.getAsMention() + "**!").setEphemeral(true).queue();

            ticketChannel.sendMessage(member.getAsMention() + guild.getRoleById(OWNER_ROLE_ID).getAsMention()).queue();
            Color minhacor = new Color(255, 255, 255);
            EmbedBuilder welcomeEmbed = new EmbedBuilder()
                    .setTitle("Bem-vindo ao canal de Compra do Lua Cheia!")
                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                    .addField("<a:almdecorbowpink:1223409342376706139> **Instruções de pagamento:**", "\n\n"
                            + "  <a:Love:1224449007204696237> Enviar pix clicando no link: https://nubank.com.br/cobrar/9855g/6618b2b8-b801-41f6-b92c-6fd33ba054ce\n\n"
                            + " <a:Love:1224449007204696237> Após enviar o pagamento, anexe o comprovante e aguarde a confirmação dos ADM's", false)
                    .addField("<a:almdecorbowpink:1223409342376706139> **Detalhes do VIP:**", "\n\n"
                            +"   <a:Love:1224449007204696237> Preço: **R$ 5,00** \n"
                            + "  <a:Love:1224449007204696237> Confira novamente os benefícios em <#1228889783137472583>\n\n"
                            + "Agradecemos por considerar a compra do VIP "+ guild.getRoleById(idcargoLuaCheia).getAsMention() + "em nosso servidor! Estamos ansiosos para "
                            + " recebê-lo como um membro VIP! <:aw:1223409367169368195> ", false)

                    .setImage("https://cdn.discordapp.com/attachments/875131727041937420/877723401219878972/image3-1-4.gif?ex=6629af9e&is=66173a9e&hm=8d572ef3e046609f63e0dedb97052e108a957aba4073e59920e823a546cbef3e&")
                    .setColor(minhacor);



            Button ticketButton = Button.primary("close_luacheia_ticket", "Fechar o Ticket agora");

            // Enviar embed com o botão
            ticketChannel.sendMessageEmbeds(welcomeEmbed.build()).setActionRow(ticketButton).queue();



        }

        if (event.getComponentId().equals("close_luacheia_ticket"))
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
