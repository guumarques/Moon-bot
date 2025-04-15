package Events;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ChristmasRole extends ListenerAdapter
{
    private static final String CHRISTMAS_CHANNEL_ID = "1321193170930896916";
    private static final String CHRISTMAS_ROLE_ID = "1321192504275636294";
    private static final String DIVINDADE_ROLE_ID = "1223394386558320680";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String content = message.getContentRaw();
        Member member = event.getMember();

        // Verificar se a mensagem foi enviada no canal de tickets
        if(event.getChannel().getType().isMessage() && event.getChannel() instanceof TextChannel channel && event.getChannel().getId().equals(CHRISTMAS_CHANNEL_ID))
        {
            Role divindade = event.getGuild().getRoleById(DIVINDADE_ROLE_ID);
            if(content.equalsIgnoreCase("!christmas") && hasRole(member, divindade ))
            {
                canalNatal(channel);
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        if (event.getComponentId().equals("christmas_confirm")) {

            // Defer a resposta da interação

            event.deferReply(true).queue();


            // Verifica se a interação é do botão "confirm"

            Member member = event.getMember();

            Guild guild = event.getGuild();


            if (member != null && guild != null) {

                Role memberRole = guild.getRoleById(CHRISTMAS_ROLE_ID);

                if (memberRole != null) {

                    // Adiciona o cargo ao membro

                    guild.addRoleToMember(member, memberRole).queue(success -> {

                        // Responde à interação com sucesso

                        event.getHook().sendMessage("Cargo " + memberRole.getAsMention() + " adicionado com sucesso! 🎉 Feliz Natal!").setEphemeral(true).queue();

                    }, failure -> {

                        // Responde à interação em caso de falha

                        event.getHook().sendMessage("Ocorreu um erro ao adicionar o cargo. Tente novamente mais tarde.").setEphemeral(true).queue();

                    });

                } else {

                    event.getHook().sendMessage("Cargo não encontrado!").setEphemeral(true).queue(); // Responde à interação

                }

            } else {

                event.getHook().sendMessage("Não foi possível encontrar suas informações de membro.").setEphemeral(true).queue();

            }

        }
    }

    private void canalNatal(MessageChannel channel)
    {
        Guild guild = channel.getJDA().getGuildById("1223392724497993778");
        Role christmasRole = guild.getRoleById(CHRISTMAS_ROLE_ID);

        // Cria uma EmbedBuilder
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🎄 Anúncio do Cargo de Natal! 🎄", null);
        embed.setDescription("🎉 O Natal está chegando e queremos celebrar com você! 🎉\n" +
                "Ganhe o nosso exclusivo " + christmasRole.getAsMention() + " e entre no clima festivo da nossa comunidade!");
        embed.setColor(Color.RED); // Define uma cor festiva
        embed.setThumbnail("https://imgur.com/3d9ZqKc.gif");
        embed.setImage("https://imgur.com/znL21zM.gif");
        embed.setFooter("Aproveite o Natal com a gente!");

        // Cria um botão
        Button button = Button.success("christmas_confirm", "Ganhar Cargo de Natal");

        // Envia a mensagem com a embed e o botão
        channel.sendMessageEmbeds(embed.build()).setActionRow(button).queue();
    }

    public static boolean hasRole(Member member, Role role)
    {
        List<Role> memberRoles = member.getRoles();
        return memberRoles.contains(role);
    }
}
