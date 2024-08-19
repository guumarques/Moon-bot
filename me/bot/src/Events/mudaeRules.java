package Events;

import java.util.Collections;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class mudaeRules extends ListenerAdapter 
{
    private static final String MUDAE_RULES_CHANNEL_ID = "1238338375765983303";
    private static final String MUDAE_ROLE_ID = "1238328216909910046";

     @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // Verificar se a mensagem foi enviada no canal de tickets
        if (event.getChannel().getId().equals(MUDAE_RULES_CHANNEL_ID)) 
        {
            if (content.equalsIgnoreCase("!mudae")) {
                openTicket(event.getGuild(), event.getMember());
            }
        }
    }

    

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) 
    {
        if (event.getComponentId().equals("confirm")) 
        {
            // Verifica se a interação é do botão "open_ticket"
            Member member = event.getMember();
            Guild guild = event.getGuild();
            
            if (member != null && guild != null) {
                Role memberRole = guild.getRoleById(MUDAE_ROLE_ID);
                if (memberRole != null) {
                    guild.addRoleToMember(member, memberRole).queue(); // Adiciona o cargo ao membro
                    event.reply("Cargo " + memberRole.getAsMention() + " adicionado com sucesso! Agora você pode acessar o canal da <#1223687581108142152>").setEphemeral(true).queue(); // Responde à interação
                } else {
                    event.reply("Cargo não encontrado!").setEphemeral(true).queue(); // Responde à interação
                }
            }
        }
    }



    private void openTicket(Guild guild, Member member) 
    {
        TextChannel ticketChannel = guild.getTextChannelById(MUDAE_RULES_CHANNEL_ID);
        if (ticketChannel == null) 
        {
            System.out.println("Canal não encontrado!");
            return;
        }

        // Criar embed para a mensagem de boas-vindas
        TextChannel textChannel = guild.getTextChannelById(MUDAE_RULES_CHANNEL_ID);
        textChannel.sendMessage("https://cdn.discordapp.com/attachments/912486443035787295/1134138357375586485/Untitled19_20230727202840.png?ex=663edf67&is=663d8de7&hm=905eb0fa5b1788b36288ce3af034cb2529d64aa7c8a118b3dbd1f76f9f511581&").queue();
        EmbedBuilder welcomeEmbed = new EmbedBuilder()
                .setTitle("Regras da Mudae!")
                .setDescription("Aqui estão algumas regras importantes para garantir a melhor experiência com a Mudae:")
                .addField("<:3464white1:1238495432053886997> **Conta Secundária:**", "É estritamente proibido o uso de contas secundárias na Mudae. O uso de contas secundárias é considerado uma violação séria, pois pode resultar em vantagens injustas. Se esta regra for violada, o membro estará sujeito às seguintes punições: \n\n "+ 
                "`Punição 1`: Essa punição será aplicada quando a regra for quebrada pela primeira vez. O membro terá todos os personagens da conta secundária divorciados, além de receber quarentena permanente na mesma conta. \n\n " + 
                "`Punição 2`: Essa punição será aplicada quando a regra for quebrada pela segunda vez. O membro terá também o harém da conta principal zerada e receberá quarentena permanente, o que equivale a um ban da mudae.", false)
                .addField("<:5673white2:1238495430279561232> **Rolls:**", "Evite ficar falando em momentos de rolls, pois isso atrapalha quem está girando. Caso essa regra seja quebrada, o membro estará sujeito à seguinte punição: \n\n " +
                "`Punição`: Mute de 1 hora no canal da mudae.", false)
                .addField("<:6334white3:1238495428824006716> **Fotos e Alias Inapropriados:**", "É estritamente proibido adicionar imagens explícitas, incluindo conteúdo adulto, nos personagens obtidos, assim como utilizar **aliases** com nomes vulgares ou ofensivos. Se esta regra for violada, o membro estará sujeito à seguinte punição: \n\n" +
                "`Punição`: Divorce no personagem em questão.", false)
                .addField("Termo de Consentimento", "Por favor, clique no botão abaixo para declarar que: \n\n " +
                " - Declaro que estou ciente das regras do servidor e que as li com atenção. \n" + 
                "- Estou disposto a receber as punições estabelecidas caso eu viole as regras.", false)
                .setColor(Color.PINK)
                .setImage("https://cdn.discordapp.com/attachments/912486443035787295/1078744460738957403/Untitled240_20210721110741.png?ex=663f0f51&is=663dbdd1&hm=a0033274f27e55e16ce642009d00c3b8ee8d1c80f482c71188fb6979efb25b01&");


        // Adicionar botão de abrir ticket
        Button ticketButton = Button.primary("confirm", "confirmar");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build())
                .setActionRow(ticketButton)
                .queue();
    }
    


        
}
