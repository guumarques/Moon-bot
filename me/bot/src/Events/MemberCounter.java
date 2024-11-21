package Events;

import Commands.ConnectionDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemberCounter extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        Role eclipse = event.getGuild().getRoleById("1223689600321454153");
        Role luaCheia = event.getGuild().getRoleById("1225963752076083231");
        Role minguante = event.getGuild().getRoleById("1225963823815458948");

        try //do ponto de vista de quem tem o vip, saiu do servidor, voltou e agora recebeu seus cargos
        {
            Member member = event.getMember();

            String query = "SELECT * FROM mensalidade WHERE idMembro = ?";
            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
            statement.setString(1, event.getUser().getId());

            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                String tipoVip = rs.getString("tipo_vip");

                if(tipoVip.equals("Eclipse"))
                {
                    event.getGuild().addRoleToMember(member, eclipse).queue();

                    String query2 = "SELECT * FROM eclipsevip WHERE memberID = ?";
                    PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                    statement2.setString(1, event.getUser().getId());

                    ResultSet rs2 = statement2.executeQuery();

                    if(rs2.next()) //o membro entrou no servidor e tinha o vip eclipse
                    {
                        String idCustomRoleEclipse = rs2.getString("idCustomRole");
                        String loverIdRoleEclipse = rs2.getString("loverIdRole");

                        Role idCustomRole = event.getGuild().getRoleById(idCustomRoleEclipse);
                        Role loverIdRole = event.getGuild().getRoleById(loverIdRoleEclipse);


                        //verificando cargos no banco de dados
                        if(idCustomRoleEclipse != null)
                        {
                            System.out.println("O membro " + member.getEffectiveName() + " estava sem o seu cargo " + "---- " + idCustomRole.getName() + " ----, acabei de atribuí-lo!");
                            event.getGuild().addRoleToMember(member, idCustomRole).queue();
                        }
                        if(loverIdRoleEclipse != null)
                        {
                            System.out.println("O membro " + member.getEffectiveName() + " recebeu novamente seu cargo de eclipselover!");
                            event.getGuild().addRoleToMember(member, loverIdRole).queue();
                        }
                    }

                    String query3 = "SELECT * FROM eclipsefriend WHERE memberID = ?";
                    PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);
                    statement3.setString(1, event.getUser().getId());

                    ResultSet rs3 = statement3.executeQuery();

                    while(rs3.next())
                    {
                        String createdFriendRoleIDEclipse = rs3.getString("createdFriendRoleID");
                        String friendIDEclipse = rs3.getString("friendID");

                        Role createdFriendRoleID = event.getGuild().getRoleById(createdFriendRoleIDEclipse);
                        Member friendID = event.getGuild().getMemberById(friendIDEclipse);

                        if(createdFriendRoleIDEclipse != null)
                        {
                            System.out.println("O membro " + friendID.getEffectiveName() + " recebeu novamente seu cargo de eclipsefriend!");
                            event.getGuild().addRoleToMember(member, createdFriendRoleID).queue();
                        }
                    }
                }
                else if(tipoVip.equals("Lua-Cheia"))
                {
                    event.getGuild().addRoleToMember(member, luaCheia).queue();

                    String query2 = "SELECT * FROM moonvip WHERE memberID = ?";
                    PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                    statement2.setString(1, event.getUser().getId());

                    ResultSet rs2 = statement2.executeQuery();

                    if(rs2.next())
                    {
                        String idCustomRoleLuaCheia = rs2.getString("idCustomRole");

                        Role idCustomRole = event.getGuild().getRoleById(idCustomRoleLuaCheia);

                        if(idCustomRoleLuaCheia != null) //criei meu cargo antes de sair do servidor
                        {
                            event.getGuild().addRoleToMember(member, idCustomRole).queue();
                            System.out.println("O membro " + member.getEffectiveName() + " recebeu o seu cargo moonvip novamente!");
                        }
                    }

                    String query3 = "SELECT * FROM moonfriend WHERE memberID = ?";
                    PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);
                    statement3.setString(1, event.getUser().getId());

                    ResultSet rs3 = statement3.executeQuery();

                    while(rs3.next())
                    {
                        String createdFriendRoleIDMoonvip = rs3.getString("createdFriendRoleID");

                        Role createdFriendRoleID = event.getGuild().getRoleById(createdFriendRoleIDMoonvip);

                        if(createdFriendRoleIDMoonvip != null)
                        {
                            event.getGuild().addRoleToMember(member, createdFriendRoleID).queue();
                            System.out.println("O membro " + member.getEffectiveName() + " recebeu o seu cargo moonfriend novamente!");
                        }
                    }
                }
                else if(tipoVip.equals("Minguante"))
                {
                    event.getGuild().addRoleToMember(member, minguante).queue();

                    String query2 = "SELECT * FROM minguantevip WHERE memberID = ?";
                    PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                    statement2.setString(1, event.getUser().getId());

                    ResultSet rs2 = statement2.executeQuery();

                    if(rs2.next())
                    {
                        String idCustomRoleMinguante = rs2.getString("idCustomRole");

                        Role idCustomRole = event.getGuild().getRoleById(idCustomRoleMinguante);

                        if(idCustomRoleMinguante != null)
                        {
                            event.getGuild().addRoleToMember(member, idCustomRole).queue();
                            System.out.println("O membro " + member.getEffectiveName() + " recebeu o seu cargo minguantevip novamente!");
                        }
                    }
                }
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        try //do ponto de vista de quem saiu do servidor, voltou sem os cargos e agora recebeu os cargos do VIP
        {
            Member member = event.getMember();

            String query = "SELECT * FROM eclipsevip WHERE loverId = ?";
            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

            statement.setString(1, event.getUser().getId());

            ResultSet rs = statement.executeQuery();

            while(rs.next())
            {
                String loverIdRoleEclipse = rs.getString("loverIdRole");
                String memberIDEclipse = rs.getString("memberID");

                Role loverIdRole = event.getGuild().getRoleById(loverIdRoleEclipse);
                Member memberComVip = event.getGuild().getMemberById(memberIDEclipse);

                if(memberComVip != null) //a pessoa que me deu vip ainda está no servidor
                {
                    event.getGuild().addRoleToMember(member, loverIdRole).queue();

                    System.out.println("O membro " + member.getEffectiveName() + ", lover de " + memberComVip.getEffectiveName() + " recebeu o seu cargo novamente!");
                }
                else
                {
                    event.getGuild().addRoleToMember(member, loverIdRole).queue();

                    System.out.println("O membro " + member.getEffectiveName() + ", lover de " + memberComVip.getEffectiveName() + ", embora o mesmo ainda não esteja no servidor, recebeu o seu cargo novamente!");
                }
            }

            String query2 = "SELECT * FROM eclipsefriend WHERE friendID = ?";
            PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
            statement2.setString(1, event.getUser().getId());

            ResultSet rs2 = statement2.executeQuery();

            while(rs2.next())
            {
                String createdFriendRoleIDEclipse = rs2.getString("createdFriendRoleID");
                String memberID = rs2.getString("memberID");

                Role createdFriendRole = event.getGuild().getRoleById(createdFriendRoleIDEclipse);
                Member memberComVip = event.getGuild().getMemberById(memberID);

                if(memberComVip != null) //a pessoa que me deu vip ainda continua no servidor
                {
                    event.getGuild().addRoleToMember(member, createdFriendRole).queue();

                    System.out.println("O membro " + member.getEffectiveName() + ", amigo de " + memberComVip.getEffectiveName() + " recebeu o seu cargo novamente!");
                }
                else //a pessoa que me deu vip não está mais no servidor
                {
                    event.getGuild().addRoleToMember(member, createdFriendRole).queue();

                    System.out.println("O membro " + member.getEffectiveName() + ", amigo de " + memberComVip.getEffectiveName() + ", embora o mesmo não esteja no servidor, recebeu o seu cargo novamente!");
                }
            }

            String query3 = "SELECT * FROM moonfriend WHERE friendID = ?";
            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);
            statement3.setString(1, event.getUser().getId());

            ResultSet rs3 = statement3.executeQuery();

            while(rs3.next())
            {
                String createdFriendRoleIDLuaCheia = rs3.getString("createdFriendRoleID");
                String memberID = rs3.getString("memberID");

                Role createdFriendRoleID = event.getGuild().getRoleById(createdFriendRoleIDLuaCheia);
                Member membroComVip = event.getGuild().getMemberById(memberID);

                if(membroComVip != null) //a pessoa que me deu vip ainda está no servidor
                {
                    System.out.println("O membro " + member.getEffectiveName() + ", amigo de " + membroComVip.getEffectiveName() + " recebeu o seu cargo novamente!");
                    event.getGuild().addRoleToMember(member, createdFriendRoleID).queue();

                }
                else
                {
                    System.out.println("O membro " + member.getEffectiveName() + ", amigo de " + membroComVip.getEffectiveName() + ", embora o mesmo não esteja no servidor, recebeu o seu cargo novamente!");
                    event.getGuild().addRoleToMember(member, createdFriendRoleID).queue();
                }

            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        //--------------------------------------------------------------------------------------------------------------------------------------------------
        String emojiDescription = "<a:9691hellokittyarrowr:1228440474977697862>";
        String cargoId = "1258136611481452595";
        int corEmbed = 16761035;
        String channelId = "1223664906138816703";
        String bichinho = "<a:PrincessBow:1299475325373386793>";
        String channelIdRegras = "1223394240319586335";

        User user = event.getUser();
        user.retrieveProfile().queue(
                perfil ->
                {
                    String bannerUrl = perfil.getBannerUrl();

                    String footerImag = event.getUser().getAvatarUrl();

                    EmbedBuilder embedRECEPCAO =  new EmbedBuilder();
                    embedRECEPCAO.setAuthor(event.getUser().getEffectiveName());
                    embedRECEPCAO.setTitle("**Seja bem-vindo(a) ao Santuário Lunar 月** " + bichinho);
                    embedRECEPCAO.setDescription(emojiDescription + " **Aproveite sua estadia no servidor e que se divirta bastante!!**\n\n" +
                            emojiDescription + " **Antes, confira as regras no canal** <#" + channelIdRegras + ">\n\n" +
                            emojiDescription + " <@&" + cargoId + "> **está aqui para tirar qualquer dúvida !!**");
                    embedRECEPCAO.setThumbnail(event.getUser().getAvatarUrl());
                    embedRECEPCAO.setImage(bannerUrl);
                    embedRECEPCAO.setColor(corEmbed);
                    embedRECEPCAO.setFooter("月 𝚂𝚊𝚗𝚝𝚞á𝚛𝚒𝚘 𝙻𝚞𝚗𝚊𝚛" , footerImag);

                    event.getGuild().getTextChannelById(channelId).sendMessage(event.getUser().getAsMention() + "<@&" + cargoId + ">").queue(
                            message -> {
                                message.delete().queueAfter(60, TimeUnit.SECONDS);
                            }
                    );
                    event.getGuild().getTextChannelById(channelId).sendMessageEmbeds(embedRECEPCAO.build()).queue(
                            sendMessageEmbeds -> {
                                sendMessageEmbeds.delete().queueAfter(60, TimeUnit.SECONDS);
                            }
                    );

                }
        );


        //--------------------------------------------------------------------------------------------------------------------------------------------------

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

    public static boolean hasRole(Member member, Role role)
    {
        List<Role> memberRoles = member.getRoles();
        return memberRoles.contains(role);
    }
}
