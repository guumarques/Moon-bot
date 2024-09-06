package Events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberCounter extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById("1223664906138816703"); // Replace with your channel ID

        if (channel != null) {
            int memberCount = guild.getMemberCount();
            String contador = Integer.toString(memberCount);

            // Emojis para formar a palavra "SOMOS"
            String[] somosEmojis = {
                    "<:9958letterm:1218423013632905226>", //M
                    "<:3442lettere:1224205656953847842>", //E
                    "<:9958letterm:1218423013632905226>", //M
                    "<:3437letterb:1224205655498297374>", //B
                    "<:1634letterr:1224205654101590026>", //R
                    "<:2264lettero:1218423015075741716>", //O
                    "<:1116letters:1218423016317517895>" //S
            };

            // Emojis para os números
            String[] numerosEmojis = {
                    "<:5445number0:1218427899367915603>",
                    "<:5494number1:1218427897438801980>",
                    "<:1784number2:1218427896176312380>",
                    "<:1501number3:1218427894569893898>",
                    "<:5857number4:1218427892879327234>",
                    "<:7234number5:1218427891822362624>",
                    "<:1257number6:1218427890010554439>",
                    "<:5807number7:1218427888039104523>",
                    "<:1027number8:1218427886554578985>",
                    "<:6098number9:1218429532466577479>"
            };

            String[] simbolos =
                    {
                            "<a:4754zodiacpisces:1218423042515140648>",
                            "<a:7821zodiaccapricorn:1218423038387945484>",
                            "<a:nitroboosterrmz1:1218437037783384114>"
                    };

            // Construindo a parte da palavra "SOMOS"
            StringBuilder somosPalavra = new StringBuilder();
            for (String somo : somosEmojis)
            {
                somosPalavra.append(somo).append(" ");
            }

            // Construindo a parte do contador de membros
            StringBuilder contadorMembros = new StringBuilder();
            String memberCountStr = Integer.toString(memberCount);

            for (char digit : memberCountStr.toCharArray())
            {
                contadorMembros.append(numerosEmojis[digit - '0']).append(" ");
            }

            // Construindo o tópico completo
            String topic = simbolos[2] + somosPalavra.toString() + simbolos[2] + simbolos[0] + contadorMembros.toString() + simbolos[1];

            // Define o tópico do canal
            System.out.println("O servidor tem " + memberCount + " membros agora");
            channel.getManager().setTopic(topic).queue();
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById("1223664906138816703"); // Replace with your channel ID

        if (channel != null) {
            int memberCount = guild.getMemberCount();
            String contador = Integer.toString(memberCount);

            // Emojis para formar a palavra "SOMOS"
            String[] somosEmojis = {
                    "<:9958letterm:1218423013632905226>", //M
                    "<:3442lettere:1224205656953847842>", //E
                    "<:9958letterm:1218423013632905226>", //M
                    "<:3437letterb:1224205655498297374>", //B
                    "<:1634letterr:1224205654101590026>", //R
                    "<:2264lettero:1218423015075741716>", //O
                    "<:1116letters:1218423016317517895>" //S
            };

            // Emojis para os números
            String[] numerosEmojis = {
                    "<:5445number0:1218427899367915603>",
                    "<:5494number1:1218427897438801980>",
                    "<:1784number2:1218427896176312380>",
                    "<:1501number3:1218427894569893898>",
                    "<:5857number4:1218427892879327234>",
                    "<:7234number5:1218427891822362624>",
                    "<:1257number6:1218427890010554439>",
                    "<:5807number7:1218427888039104523>",
                    "<:1027number8:1218427886554578985>",
                    "<:6098number9:1218429532466577479>"
            };

            String[] simbolos =
                    {
                            "<a:4754zodiacpisces:1218423042515140648>",
                            "<a:7821zodiaccapricorn:1218423038387945484>",
                            "<a:nitroboosterrmz1:1218437037783384114>"
                    };

            // Construindo a parte da palavra "SOMOS"
            StringBuilder somosPalavra = new StringBuilder();
            for (String somo : somosEmojis)
            {
                somosPalavra.append(somo).append(" ");
            }

            // Construindo a parte do contador de membros
            StringBuilder contadorMembros = new StringBuilder();
            String memberCountStr = Integer.toString(memberCount);

            for (char digit : memberCountStr.toCharArray())
            {
                contadorMembros.append(numerosEmojis[digit - '0']).append(" ");
            }

            // Construindo o tópico completo
            String topic = simbolos[2] + somosPalavra.toString() + simbolos[2] + simbolos[0] + contadorMembros.toString() + simbolos[1];

            // Define o tópico do canal
            System.out.println("O servidor tem " + memberCount + " membros agora");
            channel.getManager().setTopic(topic).queue();
        }
    }
}
