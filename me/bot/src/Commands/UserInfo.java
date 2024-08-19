package Commands;


import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import main.Moon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserInfo extends ListenerAdapter {

    // Função para traduzir os status de presença
    private String translateStatus(OnlineStatus onlineStatus) {
        switch (onlineStatus) {
            case ONLINE:
                return "Online";
            case IDLE:
                return "Ausente";
            case DO_NOT_DISTURB:
                return "Não Perturbar";
            case INVISIBLE:
                return "Invisível";
            default:
                return "Offline";
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // message spot
        String[] args = event.getMessage().getContentRaw().split(" ");

        // text channel spot
        TextChannel textChannel = event.getGuild().getTextChannelById("1223674414667792484");

        if (args.length >= 2
                && args[0].equalsIgnoreCase("!perfil")) // aqui é
                                                                                                             // pra ver
                                                                                                             // se eu
                                                                                                             // estou
                                                                                                             // botando
                                                                                                             // ID de
                                                                                                             // alguém
        {
            String mentioned = args[1]; // se eu botar mais de um ID, ele mostra o primeiro

            if (event.getChannel().getIdLong() == 1223664906138816703L) // verifico se foi digitado no chat geral
            {
                Member targetMember = null;

                try {
                    long memberId = Long.parseLong(mentioned); // converte String em Long
                    targetMember = event.getGuild().getMemberById(memberId); // pegando o ID convertido e atribuindo a
                                                                             // target member como ID
                } catch (NumberFormatException e) {
                    // Ignorar a exceção, pois o argumento não é um número válido
                }

                if (targetMember != null) // ele só vai dar null se o mentioned for um número qualquer, não sendo ID de
                                          // ninguém
                {

                    // Construir e enviar o perfil do membro alvo
                    buildUserProfile(textChannel, targetMember);
                } else {
                    textChannel.sendMessage(
                            "Usuário não encontrado. Certifique-se de que você forneceu um ID de usuário válido.")
                            .queue();
                }
            } else // vai cair aqui quando eu der o comando em outro chat, sem que seja o chat
                   // geral
            {
                textChannel.sendMessage("Você não pode usar este comando aqui!").queue();
            }
        }

        if (args.length == 1
                && args[0].equalsIgnoreCase("!perfil")) // aqui é
                                                                                                             // se a
                                                                                                             // própria
                                                                                                             // pessoa
                                                                                                             // dá o
                                                                                                             // comando
        {
            // Exibir o perfil do autor da mensagem
            Member ownMember = event.getMember();
            buildUserProfile(textChannel, ownMember); // o próprio membro digitou
        }
    }

    private void buildUserProfile(TextChannel textChannel, Member member) {
        String userName = member.getUser().getName() + "#" + member.getUser().getDiscriminator(); // aqui e o nome do
                                                                                                  // usuário, com #
        String nickName = member.getEffectiveName(); // é o nick
        long userID = member.getIdLong(); // ID do usuário
        String avatar = member.getEffectiveAvatarUrl(); // avatar(perfil)
        String status = translateStatus(member.getOnlineStatus()); // status(online, ocupado, invisível ou ausente)

        OffsetDateTime joinDate = member.getTimeJoined(); // recebendo informações do dia que entrou no servidor
        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm"); // atribuindo o padrão do jeito que
                                                                                // quero que a data e hora fique,
                                                                                // setando dois dígitos para hora e
                                                                                // minutos
        String userJoinDate = formatar.format(Date.from(joinDate.toInstant())); // aqui eu estou formatando a data que o
                                                                                // membro entrou no padrão que solicitei

        // embed
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Informações do membro " + nickName, "https://www.google.com", avatar);
        embedBuilder.setThumbnail(avatar);
        embedBuilder.setColor(Color.BLACK);
        embedBuilder.addField("<:Sv_GoldenPig:1173972866270760981> Tag", userName, true);
        embedBuilder.addField("<:Sv_ID:1173972868049141780> ID", String.valueOf(userID), true);

        if (status.equals("Online")) {
            embedBuilder.addField("<:Sv_ToggleON:1173972872167964765> Status", status, true);
        } else if (status.equals("Ausente")) {
            embedBuilder.addField("<:Sv_ToggleON_Yellow:1173972882125238292> Status", status, true);
        }

        else if (status.equals("Não Perturbar")) {
            embedBuilder.addField("<:Sv_ToggleON_Red:1173972880686587924> Status" + " Status", status, true);
        }

        else if (status.equals("Invisível")) {
            embedBuilder.addField("<:Sv_ToggleON_Gray:1173972877293395989> Status", status, true);
        }

        else {
            embedBuilder.addField("<:Sv_ToggleON_Gray:1173972877293395989> Status", status, true);
        }
        embedBuilder.appendDescription("Entrou no dia " + userJoinDate);

        // pegando as atividade e status personalizados
        List<Activity> activities = member.getActivities();

        if (!activities.isEmpty()) {
            // Se houver atividades, adicione-as ao EmbedBuilder
            StringBuilder userActivities = new StringBuilder();

            for (Activity activity : activities) {
                if (activity.getType() != Activity.ActivityType.CUSTOM_STATUS) {
                    userActivities.append(activity.getName()).append("\n");
                }
            }

            if (userActivities.length() > 0) {
                // Se houver atividades não personalizadas, adicione-as
                embedBuilder.addField("<:Sv_infoMessage:1173972870175662112> Atividade", userActivities.toString(),
                        true);
            }
        }

        // Verifique se há customStatus
        String customStatus = "";
        for (Activity activity : activities) {
            if (activity.getType() == Activity.ActivityType.CUSTOM_STATUS) {
                customStatus = activity.getName();
                break; // Pare assim que encontrar o primeiro customStatus
            }
        }

        if (!customStatus.isEmpty()) {
            embedBuilder.addField("<:Sv_Comment:1173972863687065631> Status Personalizado", customStatus, true);
        }
        MessageEmbed embed = embedBuilder.build();
        textChannel.sendMessageEmbeds(embed).queue();
    }
}