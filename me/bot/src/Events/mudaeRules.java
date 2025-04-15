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
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("mudae_confirm")) {
            // Verifica se a interação é do botão "confirm"
            Member member = event.getMember();
            Guild guild = event.getGuild();

            event.deferReply(true).queue();
            if (member != null && guild != null) {
                Role memberRole = guild.getRoleById(MUDAE_ROLE_ID);
                if (memberRole != null) {
                    guild.addRoleToMember(member, memberRole).queue(success -> {
                        event.getHook().sendMessage("Cargo " + memberRole.getAsMention() + " adicionado com sucesso! Agora você pode acessar o canal da <#1223687581108142152>").setEphemeral(true).queue();
                    }, failure -> {
                        event.getHook().sendMessage("Ocorreu um erro ao adicionar o cargo.").setEphemeral(true).queue();
                    });
                } else {
                    event.getHook().sendMessage("Cargo não encontrado!").setEphemeral(true).queue(); // Responde à interação
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
        textChannel.sendMessage("https://cdn.discordapp.com/attachments/1280357153085067347/1327104100164309004/image4.gif?ex=6781d981&is=67808801&hm=73705537d19e7765647447c63d11fcdd0393ff29d0ac28aecd8494674f3be0f8&").queue();
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
                .setImage("https://cdn.discordapp.com/attachments/1280357153085067347/1327104993270038588/image0-2-1.gif?ex=6781da56&is=678088d6&hm=1ec33b1a0236d8be5a5cb7a2b67ea6f128397d94905422d366f8f759fd3c2338&");


        // Adicionar botão de abrir ticket
        Button ticketButton = Button.primary("mudae_confirm", "confirmar");

        // Enviar embed com o botão
        ticketChannel.sendMessageEmbeds(welcomeEmbed.build())
                .setActionRow(ticketButton)
                .queue();
    }




}
