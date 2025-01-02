package Events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;

public class NewRoles extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (event.getChannel().getId().equals("1320396668889464912"))
        {
            if (content.equalsIgnoreCase("!newroles"))
            {
                sendRolesSelection(event.getChannel());
            }
        }
    }

    public void sendRolesSelection(MessageChannel channel)
    {
        channel.sendMessage("Bem-vindo ao canal de cargos! Aqui você pode escolher quais notificações deseja receber e quais jogos gostaria de jogar com a nossa comunidade.").queue();

        // Primeira embed
        EmbedBuilder embed1 = new EmbedBuilder();
        embed1.setTitle("Quais notificações você gostaria de receber dentro do Santuário?");
        embed1.setColor(Color.decode("#fecfd0"));

        // Criação do SelectMenu para notificações
        StringSelectMenu selectMenuNotificacoes = StringSelectMenu.create("notificacoes_select")
                .setPlaceholder("Selecione as notificações que deseja receber")
                .addOption("\uD83C\uDF89│Ping Eventos", "1257856454568968212")
                .addOption("\uD83C\uDF89│Ping Sorteios", "1262194026895904859")
                .addOption("\uD83D\uDD14│Ping Parcerias", "1257307036933296149")
                .addOption("\uD83D\uDC7B│Ping Reviver chat", "1257308526854934589")
                .addOption("⭐│Ping bump", "1240404956947812414")
                .addOption("❌│Não receber notificação", "1262211504879570976")
                .build();

        // Envia a primeira embed com o SelectMenu
        channel.sendMessageEmbeds(embed1.build())
                .addActionRow(selectMenuNotificacoes) // Adiciona o SelectMenu
                .queue();

        // Segunda embed
        EmbedBuilder embed2 = new EmbedBuilder();
        embed2.setTitle("Quais jogos você tem interesse em estar jogando junto a nossa comunidade?");
        embed2.setColor(Color.decode("#fecfd0"));


        // Criação do SelectMenu para jogos
        StringSelectMenu selectMenuJogos = StringSelectMenu.create("jogos_select")
                .setPlaceholder("Selecione os jogos que você gostaria de jogar")
                .addOption("Minecraft", "1224517637107879946")
                .addOption("Free Fire", "1224517588835500182")
                .addOption("Brawl stars", "1224517645739626651")
                .addOption("Genshin", "1224518018449801247")
                .addOption("Vava", "1224517636910616638")
                .addOption("Lol", "1224517652341325905")
                .addOption("Roblox", "1224517630681944195")
                .build();

        // Envia a segunda embed com o SelectMenu
        channel.sendMessageEmbeds(embed2.build())
                .addActionRow(selectMenuJogos) // Adiciona o SelectMenu
                .queue();
    }

    @Override
    public void onGenericSelectMenuInteraction(GenericSelectMenuInteractionEvent event)
    {
        String menuId = event.getComponentId();
        Member member = event.getMember();

        if (menuId.equals("notificacoes_select"))
        {

            for (Object selectedValue : event.getValues())
            {
                toggleRole(member, (String) selectedValue, event); // Chama o método para adicionar/remover o cargo
            }

        } else if (menuId.equals("jogos_select"))
        {
            for (Object selectedValue : event.getValues())
            {
                toggleRole(member, (String) selectedValue, event); // Chama o método para adicionar/remover o cargo
            }
        }
    }

    private void toggleRole(Member member, String roleID, GenericSelectMenuInteractionEvent event)
    {
        // Obtém o cargo pelo ID

        Role role = member.getGuild().getRoleById(roleID);

        // Verifica se o cargo foi encontrado
        if (role == null)
        {
            member.getGuild().getTextChannels().get(0).sendMessage("Cargo não encontrado: " + role.getName()).queue();
            return; // Retorna se o cargo não foi encontrado

        }

        // Verifica se o membro já possui o cargo
        if (member.getRoles().contains(role))
        {
            // Remove o cargo
            member.getGuild().removeRoleFromMember(member, role).queue();
        }
        else
        {
            // Adiciona o cargo
            member.getGuild().addRoleToMember(member, role).queue();
            event.reply("Você recebeu o cargo: **" + role.getName() + "**!").setEphemeral(true).queue();
        }
    }
}
