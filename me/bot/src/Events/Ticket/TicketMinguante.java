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

public class TicketMinguante extends ListenerAdapter
{
    private static final String OWNER_ROLE_ID = "1223394386558320680";
    private static final String TICKET_CHANNEL_MINGUANTE_ID = "1228889937617883166";
    private static final String VIP_CATEGORY_ID = "1228889229816369264";

    String idcargoMinguante = "1225963823815458948";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // Verificar se a mensagem foi enviada no canal de tickets
        if (event.getChannel().getId().equals(TICKET_CHANNEL_MINGUANTE_ID)) {
            if (content.equalsIgnoreCase("!openticket")) {
                openTicket(event.getGuild(), event.getMember());
            }
        }
    }

    private void openTicket(Guild guild, Member member) {
        TextChannel ticketChannel = guild.getTextChannelById(TICKET_CHANNEL_MINGUANTE_ID);
        if (ticketChannel == null) {
            System.out.println("Canal de ticket não encontrado!");
            return;
        }

        // Criar embed para a mensagem de boas-vindas
        Color minhacor = new Color(0, 0, 0);
        EmbedBuilder welcomeEmbed = new EmbedBuilder()
                .setAuthor("››>> Vip's Santuário Lunar <<‹‹")
                .setTitle("<:8283moon3:1228440485174185985> " + " Vip Minguante")
                .setDescription("Quais os beneficios desse Vip? Veja logo abaixo! \n")
                .addField("<:vip_hexagon:1344080477899980820> Cargo Personalizável",
                        "<a:Seta_preta:1261768702865444935> Criar e editar **minguantevip**\n", false)

                .addField("<:black_gift:1344082392045977691> Outros Benefícios",
                        "<a:Seta_preta:1261768702865444935>  Enviar imagens no **chat geral**\n", false)

                .addField("<a:Black_Crystal_Heart:1344098358591094784> Descrição do Cargo:",
                        "<a:Seta_preta:1261768702865444935> **minguantevip** - Cargo **único**.\n", false)
                .setColor(minhacor)
                .setImage("https://imgur.com/BjfADTT.gif")
                .setThumbnail("https://cdn.discordapp.com/attachments/1118914929399955538/1228607288428990504/minguante.png?ex=662ca8b3&is=661a33b3&hm=aa2cb804a654d6f323d8c3df552e9349dff1bdb5ac36c880482fa3984bd14377&")
                .addField("Cargo : ", guild.getRoleById(idcargoMinguante).getAsMention(), false);

        TextChannel textChannel = guild.getTextChannelById(TICKET_CHANNEL_MINGUANTE_ID);

        if (textChannel != null) {
            textChannel.sendMessageEmbeds(welcomeEmbed.build()).queue();
        } else {
            System.out.println("Canal não encontrado.");
        }


        // Adicionar botão de abrir ticket
        Button ticketButton = Button.primary("open_minguante_ticket", "Comprar agora");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build())
                .setActionRow(ticketButton)
                .queue();
    }



    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        super.onButtonInteraction(event);
        if (event.getButton().getId().equals("open_minguante_ticket")) {
            Guild guild = event.getGuild();
            Member member = event.getMember();
            Category supportCategory = guild.getCategoryById(VIP_CATEGORY_ID);

            // Criar canal de ticket
            TextChannel ticketChannel = guild.createTextChannel("minguante-vip-" + member.getUser().getName())
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
                    .setTitle("Bem-vindo ao canal de Compra do Minguante!")
                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                    .addField("<a:almdecorbowpink:1223409342376706139> **Instruções de pagamento:**", "\n\n"
                            + "  <a:Love:1224449007204696237> Enviar pix clicando no link: https://nubank.com.br/cobrar/9855g/6618b2e0-de50-4c13-8bae-5c1163e18621\n\n"
                            + " <a:Love:1224449007204696237> Após enviar o pagamento, anexe o comprovante e aguarde a confirmação dos ADM's", false)
                    .addField("<a:almdecorbowpink:1223409342376706139> **Detalhes do VIP:**", "\n\n"
                            +"   <a:Love:1224449007204696237> Preço: **R$ 3,00** \n"
                            + "  <a:Love:1224449007204696237> Confira novamente os benefícios em <#1228889937617883166>\n\n"
                            + "Agradecemos por considerar a compra do VIP "+ guild.getRoleById(idcargoMinguante).getAsMention() + "em nosso servidor! Estamos ansiosos para "
                            + " recebê-lo como um membro VIP! <:aw:1223409367169368195> ", false)

                    .setImage("https://cdn.discordapp.com/attachments/875131727041937420/877723401219878972/image3-1-4.gif?ex=6629af9e&is=66173a9e&hm=8d572ef3e046609f63e0dedb97052e108a957aba4073e59920e823a546cbef3e&")
                    .setColor(minhacor);



            Button ticketButton = Button.primary("close_minguante_ticket", "Fechar o Ticket agora");

            // Enviar embed com o botão
            ticketChannel.sendMessageEmbeds(welcomeEmbed.build()).setActionRow(ticketButton).queue();



        }

        if (event.getComponentId().equals("close_minguante_ticket"))
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
