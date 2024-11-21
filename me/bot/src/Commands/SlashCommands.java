package Commands;

import EmbedMaker.EmbedMaker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import javax.imageio.ImageIO;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class SlashCommands extends ListenerAdapter
{

    EmbedBuilder Emberajuda = new EmbedBuilder();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        String command = event.getName();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        //-----------------------------------------------------------------
        Category eclipseCattegory = guild.getCategoryById("1225962188318179368");
        Category luaCheiaCattegory = guild.getCategoryById("1235368221423566929");
        //-----------------------------------------------------------------
        Role eclipse = event.getGuild().getRoleById("1223689600321454153");
        var hasEclipseRole = hasRole(member, eclipse);
        //-----------------------------------------------------------------
        Role luacheia = event.getGuild().getRoleById("1225963752076083231");
        var hasLuaCheiaRole = hasRole(member, luacheia);
        //-----------------------------------------------------------------
        Role minguante = event.getGuild().getRoleById("1225963823815458948");
        var hasMinguante = hasRole(member, minguante);
        //-----------------------------------------------------------------
        if(command.equals("contador"))
        {
            String subcommand = event.getSubcommandName();
            if(subcommand.equals("rank"))
            {
                try
                {
                    String query = "SELECT idMembro, nome, contador FROM counter ORDER BY contador DESC";
                    Statement statement = ConnectionDB.getBumpConnection().createStatement();

                    ResultSet rs = statement.executeQuery(query);

                    StringBuilder ranking = new StringBuilder();

                    int position = 1;
                    while (rs.next())
                    {
                        String nome = rs.getString("nome");
                        String memberID = rs.getString("idMembro");
                        int bumps = rs.getInt("contador");

                        // Adicionar o ranking na StringBuilder
                        ranking.append("**"+ position + "** - **" + nome + "** (" + memberID + ") ** - " + bumps + "** **bumps**\n");
                        position++;
                    }

                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                            .setTitle("<a:vssparkly:1274804830220587140> **Rank Bump** <a:vssparkly:1274804830220587140>")
                            .setDescription(ranking.toString())
                            .setColor(Color.yellow)
                            .setFooter("Bump Ranking - Atualizado em " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (command.equals("falar"))
        {
            if (event.getOption("texto") != null) {
                String texto = event.getOption("texto").getAsString();
                event.reply(texto).queue();
            }
        }

        if(command.equals("setar"))
        {
            String subcommand = event.getSubcommandName();

            Role divindadeRole = event.getGuild().getRoleById("1223394386558320680");

            if(subcommand.equals("eclipsevip"))
            {
                if(hasRole(member, divindadeRole))
                {
                    User user = event.getOption("nome").getAsUser();

                    try
                    {
                        String query = "SELECT idMembro from mensalidade where idMembro = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, user.getId());
                        ResultSet rs = statement.executeQuery();

                        if(rs.next()) //caso o usuário estiver no banco de dados, significa que ele já comprou o vip
                        {
                            event.reply("Você já colocou o cargo de **VIP** no membro `" + user.getEffectiveName() + "`").queue();
                        }
                        else
                        {
                            event.getGuild().addRoleToMember(user, eclipse).queue();

                            try
                            {

                                //---------------------------------------------------------------------------------------
                                DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                                LocalDate dataInicio = LocalDate.now();
                                LocalDate dataFinal = dataInicio.plusDays(30);
                                String dataInicioFormatado = formatar.format(dataInicio);
                                String dataFinalFormatado = formatar.format(dataFinal);
                                //----------------------------------------------------------------------------------------

                                EmbedBuilder embedCriar = new EmbedBuilder()
                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                        .setTitle("<:angel_bow:1276351888975200378> " + user.getEffectiveName() + " ganhou VIP <:angel_bow:1276351888975200378>")
                                        .setDescription("**" + user.getEffectiveName() + "** acaba de receber o cargo de **VIP**! Não esqueça de checar novamente os benefícios!")
                                        .setThumbnail(user.getEffectiveAvatarUrl())
                                        .addField("`VIP`", eclipse.getAsMention(), true)
                                        .addField("`Começa em`", "**" + dataInicioFormatado + "**", true)
                                        .addField("`Termina em`", "**" + dataFinalFormatado + "**", true)
                                        .setImage("https://cdn.discordapp.com/attachments/1118914929399955538/1276349203156701194/aesthetic-banner.gif?ex=66c93453&is=66c7e2d3&hm=6652ff3bf709553e041ad98e4c9235dad4b62e660eccce571729db2bbf79884b&")
                                        .setColor(Color.decode("#f4b8be"));

                                event.replyEmbeds(embedCriar.build()).queue();

                                String query_2 = "INSERT INTO mensalidade(nome, idMembro, data_inicio, data_final, tipo_vip) VALUES (?, ?, ?, ?, ?)";
                                PreparedStatement statement_2 = ConnectionDB.getConexao().prepareStatement(query_2);

                                statement_2.setString(1, user.getEffectiveName());
                                statement_2.setString(2, user.getId());
                                statement_2.setString(3, dataInicioFormatado);
                                statement_2.setString(4, dataFinalFormatado);
                                statement_2.setString(5, "Eclipse");

                                int rowsAffected = statement_2.executeUpdate();

                                if(rowsAffected > 0)
                                {
                                    System.out.println("Mensalidade de " + user.getEffectiveName() + " registrada com sucesso");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao registrar a mensalidade de " + user.getEffectiveName());
                                }

                            }
                            catch(SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    event.reply("Lamento, você não tem permissões suficientes para executar esse comando!").queue();
                }
            }
            else if(subcommand.equals("moonvip"))
            {
                if(hasRole(member, divindadeRole))
                {
                    User user = event.getOption("nome").getAsUser();

                    try
                    {
                        String query = "SELECT idMembro from mensalidade where idMembro = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, user.getId());
                        ResultSet rs = statement.executeQuery();

                        if(rs.next()) //caso o usuário estiver no banco de dados, significa que ele já comprou o vip
                        {
                            event.reply("Você já colocou o cargo de **VIP** no membro `" + user.getEffectiveName() + "`").queue();
                        }
                        else
                        {
                            event.getGuild().addRoleToMember(user, luacheia).queue();

                            try
                            {

                                //---------------------------------------------------------------------------------------
                                DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                                LocalDate dataInicio = LocalDate.now();
                                LocalDate dataFinal = dataInicio.plusDays(30);
                                String dataInicioFormatado = formatar.format(dataInicio);
                                String dataFinalFormatado = formatar.format(dataFinal);
                                //----------------------------------------------------------------------------------------

                                EmbedBuilder embedCriar = new EmbedBuilder()
                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                        .setTitle("<:angel_bow:1276351888975200378> " + user.getEffectiveName() + " ganhou VIP <:angel_bow:1276351888975200378>")
                                        .setDescription("**" + user.getEffectiveName() + "** acaba de receber o cargo de **VIP**! Não esqueça de checar novamente os benefícios!")
                                        .setThumbnail(user.getEffectiveAvatarUrl())
                                        .addField("`VIP`", luacheia.getAsMention(), true)
                                        .addField("`Começa em`", "**" + dataInicioFormatado + "**", true)
                                        .addField("`Termina em`", "**" + dataFinalFormatado + "**", true)
                                        .setImage("https://cdn.discordapp.com/attachments/1118914929399955538/1276349203156701194/aesthetic-banner.gif?ex=66c93453&is=66c7e2d3&hm=6652ff3bf709553e041ad98e4c9235dad4b62e660eccce571729db2bbf79884b&")
                                        .setColor(Color.decode("#f4b8be"));

                                event.replyEmbeds(embedCriar.build()).queue();

                                String query_2 = "INSERT INTO mensalidade(nome, idMembro, data_inicio, data_final, tipo_vip) VALUES (?, ?, ?, ?, ?)";
                                PreparedStatement statement_2 = ConnectionDB.getConexao().prepareStatement(query_2);

                                statement_2.setString(1, user.getEffectiveName());
                                statement_2.setString(2, user.getId());
                                statement_2.setString(3, dataInicioFormatado);
                                statement_2.setString(4, dataFinalFormatado);
                                statement_2.setString(5, "Lua-Cheia");

                                int rowsAffected = statement_2.executeUpdate();

                                if(rowsAffected > 0)
                                {
                                    System.out.println("Mensalidade de " + user.getEffectiveName() + " registrada com sucesso");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao registrar a mensalidade de " + user.getEffectiveName());
                                }

                            }
                            catch(SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    event.reply("Lamento, você não tem permissões suficientes para executar esse comando!").queue();
                }
            }
            else if(subcommand.equals("minguantevip"))
            {
                if(hasRole(member, divindadeRole))
                {
                    User user = event.getOption("nome").getAsUser();

                    try
                    {
                        String query = "SELECT idMembro from mensalidade where idMembro = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, user.getId());
                        ResultSet rs = statement.executeQuery();

                        if(rs.next()) //caso o usuário estiver no banco de dados, significa que ele já comprou o vip
                        {
                            event.reply("Você já colocou o cargo de **VIP** no membro `" + user.getEffectiveName() + "`").queue();
                        }
                        else
                        {
                            event.getGuild().addRoleToMember(user, minguante).queue();

                            try
                            {

                                //---------------------------------------------------------------------------------------
                                DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                                LocalDate dataInicio = LocalDate.now();
                                LocalDate dataFinal = dataInicio.plusDays(30);
                                String dataInicioFormatado = formatar.format(dataInicio);
                                String dataFinalFormatado = formatar.format(dataFinal);
                                //----------------------------------------------------------------------------------------

                                EmbedBuilder embedCriar = new EmbedBuilder()
                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                        .setTitle("<:angel_bow:1276351888975200378> " + user.getEffectiveName() + " ganhou VIP <:angel_bow:1276351888975200378>")
                                        .setDescription("**" + user.getEffectiveName() + "** acaba de receber o cargo de **VIP**! Não esqueça de checar novamente os benefícios!")
                                        .setThumbnail(user.getEffectiveAvatarUrl())
                                        .addField("`VIP`", minguante.getAsMention(), true)
                                        .addField("`Começa em`", "**" + dataInicioFormatado + "**", true)
                                        .addField("`Termina em`", "**" + dataFinalFormatado + "**", true)
                                        .setImage("https://cdn.discordapp.com/attachments/1118914929399955538/1276349203156701194/aesthetic-banner.gif?ex=66c93453&is=66c7e2d3&hm=6652ff3bf709553e041ad98e4c9235dad4b62e660eccce571729db2bbf79884b&")
                                        .setColor(Color.decode("#f4b8be"));

                                event.replyEmbeds(embedCriar.build()).queue();

                                String query_2 = "INSERT INTO mensalidade(nome, idMembro, data_inicio, data_final, tipo_vip) VALUES (?, ?, ?, ?, ?)";
                                PreparedStatement statement_2 = ConnectionDB.getConexao().prepareStatement(query_2);

                                statement_2.setString(1, user.getEffectiveName());
                                statement_2.setString(2, user.getId());
                                statement_2.setString(3, dataInicioFormatado);
                                statement_2.setString(4, dataFinalFormatado);
                                statement_2.setString(5, "Minguante");

                                int rowsAffected = statement_2.executeUpdate();

                                if(rowsAffected > 0)
                                {
                                    System.out.println("Mensalidade de " + user.getEffectiveName() + " registrada com sucesso");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao registrar a mensalidade de " + user.getEffectiveName());
                                }

                            }
                            catch(SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    event.reply("Lamento, você não tem permissões suficientes para executar esse comando!").queue();
                }
            }
        }

        if(command.equals("mudae"))
        {
            String subcommand = event.getSubcommandName();
            event.deferReply().queue();
            if(subcommand.equals("addquarentena"))
            {

                User user = event.getOption("nome").getAsUser();

                Role quarentineRole = event.getGuild().getRoleById("1238263034133217300");
                Role mudaePlayerRole = event.getGuild().getRoleById("1238328216909910046");
                Role divindadeRole = event.getGuild().getRoleById("1223394386558320680");

                if(hasRole(member, divindadeRole))
                {
                    event.getGuild().removeRoleFromMember(user, mudaePlayerRole).queue();
                    event.getGuild().addRoleToMember(user, quarentineRole).queue();

                    event.getHook().sendMessage("Quarentena aplicada permanentemente ao membro " + user.getAsMention()).queue();
                }
                else
                {
                    event.getHook().sendMessage("Você não tem acesso a esse comando!").queue();
                }
            }
            else if(subcommand.equals("removerquarentena"))
            {
                User user = event.getOption("nome").getAsUser();

                Member memberzinho = event.getGuild().getMemberById(user.getId());
                Role timeoutRole = event.getGuild().getRoleById("1238523692175331348");
                Role quarentineRole = event.getGuild().getRoleById("1238263034133217300");
                Role mudaePlayerRole = event.getGuild().getRoleById("1238328216909910046");
                Role divindadeRole = event.getGuild().getRoleById("1223394386558320680");

                event.deferReply().queue();

                if(hasRole(member, divindadeRole))
                {
                    if(hasRole(memberzinho, timeoutRole))
                    {
                        event.getHook().sendMessage("Usuário em timeout. Não foi possível executar esse comando!").setEphemeral(true).queue();
                    }

                    else
                    {
                        event.getGuild().removeRoleFromMember(user, quarentineRole).queue();
                        event.getGuild().addRoleToMember(user, mudaePlayerRole).queue();

                        event.getHook().sendMessage("Quarentena removida do membro " + user.getAsMention()).queue();
                    }
                }
                else
                {
                    event.getHook().sendMessage("Você não tem acesso a esse comando!").setEphemeral(true).queue();
                }
            }

            else if(subcommand.equals("addtimeout"))
            {
                final long TIMEOUT_DURATION = 3600000; // 1 hora em milissegundos
                User user = event.getOption("nome").getAsUser();

                Role timeoutRole = event.getGuild().getRoleById("1238523692175331348");
                Role mudaePlayerRole = event.getGuild().getRoleById("1238328216909910046");
                Role divindadeRole = event.getGuild().getRoleById("1223394386558320680");

                // ---------------------------------------------------------------------------------------
                // Define o formato da data e hora
                SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                // Obtém a data e hora atual e formata
                Date data = new Date();
                String dataFormatada = formatar.format(data);

                // ---------------------------------------------------------------------------------------
                event.deferReply().queue();

                if(hasRole(member, divindadeRole))
                {
                    event.getGuild().removeRoleFromMember(user, mudaePlayerRole).queue();
                    event.getGuild().addRoleToMember(user, timeoutRole).queue();

                    event.getHook().sendMessage("Timeout aplicado ao membro " + user.getAsMention() + ". Punição registrada no canal <#1223685697794347069>").queue();

                    TextChannel textChannel = event.getGuild().getTextChannelById("1223685697794347069");
                    EmbedBuilder embedCriar = new EmbedBuilder()
                            .setTitle("Registro de Punição - **Mudae**")
                            .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                            .addField("Caso:", "`2`<:expHideMutedChannelsChecked:1238896750383857714>", true)
                            .addField("Tipo:", "`+ Mudae Timeout`", true)
                            .addField("Adm:", "`" + member.getEffectiveName() + "`<:expHypesquadEvents:1238899750955258069>", true)
                            .addField("<a:2952arrow:1228440481504170097> Punido:", "`" + user.getEffectiveName() + "`<:00hellokittyneon:1238906866281746473>", true)
                            .addField("Motivo:", "`Rolls`<:angel_bow:1238906978340700180> ", true)
                            .setFooter(dataFormatada)
                            .setColor(Color.PINK);

                    textChannel.sendMessageEmbeds(embedCriar.build()).queue();

                    // Inicia o timer para remover o cargo após o tempo especificado
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            // Remove o cargo de timeout do membro
                            event.getGuild().removeRoleFromMember(user, timeoutRole).queue();
                            event.getGuild().addRoleToMember(user, mudaePlayerRole).queue();
                            event.getChannel().sendMessage("O timeout de " + user.getAsMention() + " expirou.").queue();
                        }
                    }, TIMEOUT_DURATION);

                }
                else
                {
                    event.getHook().sendMessage("Você não tem acesso a esse comando!").setEphemeral(true).queue();
                }
            }

            else if(subcommand.equals("removertimeout"))
            {
                User user = event.getOption("nome").getAsUser();

                Member memberzinho = event.getGuild().getMemberById(user.getId());
                Role quarentineRole = event.getGuild().getRoleById("1238263034133217300");
                Role timeoutRole = event.getGuild().getRoleById("1238523692175331348");
                Role mudaePlayerRole = event.getGuild().getRoleById("1238328216909910046");
                Role divindadeRole = event.getGuild().getRoleById("1223394386558320680");

                event.deferReply().queue();

                if(hasRole(member, divindadeRole))
                {
                    if(hasRole(memberzinho, quarentineRole))
                    {
                        event.getHook().sendMessage("Usuário em quarentena. Não foi possível executar esse comando!").setEphemeral(true).queue();
                    }

                    else
                    {
                        event.getGuild().removeRoleFromMember(user, timeoutRole).queue();
                        event.getGuild().addRoleToMember(user, mudaePlayerRole).queue();

                        event.getHook().sendMessage("Timeout removido do membro " + user.getAsMention()).queue();
                    }
                }
                else
                {
                    event.getHook().sendMessage("Você não tem acesso a esse comando!").setEphemeral(true).queue();
                }
            }
        }

        event.deferReply(true).queue();

        if(command.equals("comandos"))
        {

            String subcommand = event.getSubcommandName();

            if(subcommand.equals("eclipse"))
            {
                if(!hasEclipseRole)
                {
                    event.getHook().sendMessage("Você não tem permissão para usar este comando. Será necessário obter o **VIP Eclipse**").queue();
                }
                else if(hasEclipseRole && !hasLuaCheiaRole && !hasMinguante)
                {
                    Color minhacor = new Color(255,65,96,255);
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**<<<<< Instruções de Comandos do VIP Eclipse >>>>>**")
                            .setDescription(
                                    "Antes de explicar cada comando, será necessário utilizar algum site que forneça as cores em valores hexadecimal, seguido de `#`. " +
                                            "Vocês podem usar o site que preferirem, basta pesquisar `seletor de cores` no Google. Aqui está um site popular atualmente: \n\n" +
                                            "[Image Color Picker](https://imagecolorpicker.com/pt-pt)\n\n" +
                                            "Caso tenham alguma dúvida de como utilizar, vocês podem pedir ajuda aos staffs ou donos do servidor.\n\n"
                            )
                            .addField("", "**[Comandos](https://www)**", false)
                            .addField(
                                    "`/criar eclipsevip`:",
                                    "Este comando serve para criar seu cargo único no servidor, onde ninguém mais terá acesso além de você. " +
                                            "Por favor, não peça para nenhum administrador do servidor atribuir este cargo a outra pessoa. " +
                                            "Os benefícios para este cargo são: nome, emoji e cor à sua escolha. Os emojis também podem ser de outro servidor, contanto que você tenha nitro.\n" +
                                            "-# <a:critical29:1280914925727776801> **Aviso** <a:critical29:1280914925727776801>: Não coloquem emojis inapropriados. Os membros que fizerem isso estarão sujeitos a punições!",
                                    false
                            )
                            .addField(
                                    "`/editar eclipsevip`:",
                                    "Como o próprio comando sugere, ele é utilizado para editar o cargo criado anteriormente. " +
                                            "Nome, emoji e cor podem ser editados, da mesma forma que foi informado anteriormente.",
                                    false
                            )
                            .addField(
                                    "`/criar eclipsecall`:",
                                    "Este comando serve para criar sua call. Para criá-la, você deve ter criado os cargos `eclipsevip`, `eclipsefriend` e `eclipselover`, os quais são descritos abaixo. " +
                                            "Nesta call, apenas as pessoas que têm a sua tag compartilhada `eclipsefriend` possuem acesso.",
                                    false
                            )
                            .addField(
                                    "`/criar eclipselover`:",
                                    "Este comando serve para criar um cargo para quem você gostaria de compartilhar, como namorado(a), ou talvez algum amigo especial.",
                                    false
                            )
                            .addField(
                                    "`/editar eclipselover`:",
                                    "Este comando serve para editar o cargo criado anteriormente.",
                                    false
                            )
                            .addField(
                                    "`/criar eclipsefriend`:",
                                    "Cria um cargo que pode ser compartilhado com amigos. Ele possui um limite de 12 pessoas, então não será possível dar este cargo para mais de 12 pessoas.",
                                    false
                            )
                            .addField(
                                    "`/editar eclipsefriend`:",
                                    "Edita o cargo criado anteriormente.",
                                    false
                            )
                            .addField(
                                    "`/give eclipsefriend`:",
                                    "Este comando serve para atribuir o cargo criado para a pessoa que desejar.",
                                    false
                            )
                            .addField(
                                    "`/remove eclipsefriend`:",
                                    "Este comando serve para remover o cargo que foi atribuído à pessoa desejada.",
                                    false
                            )
                            .setColor(minhacor);

                    event.getHook().sendMessageEmbeds(embed.build()).queue();


                }
            }
            else if(subcommand.equals("luacheia"))
            {
                if(!hasLuaCheiaRole)
                {
                    event.getHook().sendMessage("Você não tem permissão para usar este comando. Será necessário obter o **VIP Lua-Cheia**").queue();
                }
                else if(hasLuaCheiaRole && !hasEclipseRole && !hasMinguante)
                {
                    Color minhacor = new Color(252,238,81,255);
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**<<<<< Instruções de Comandos do VIP Lua-Cheia >>>>>**")
                            .setDescription(
                                    "Antes de explicar cada comando, será necessário utilizar algum site que forneça as cores em valores hexadecimal, seguido de `#`. " +
                                            "Vocês podem usar o site que preferirem, basta pesquisar `seletor de cores` no Google. Aqui está um site popular atualmente: \n\n" +
                                            "[Image Color Picker](https://imagecolorpicker.com/pt-pt)\n\n" +
                                            "Caso tenham alguma dúvida de como utilizar, vocês podem pedir ajuda aos staffs ou donos do servidor.\n\n"
                            )
                            .addField("", "**[Comandos](https://www)**", false)
                            .addField(
                                    "`/moonvip criar`:",
                                    "Este comando serve para criar seu cargo único no servidor, onde ninguém mais terá acesso além de você. " +
                                            "Por favor, não peça para nenhum administrador do servidor atribuir este cargo a outra pessoa. " +
                                            "Os benefícios para este cargo são: nome, emoji e cor à sua escolha. Os emojis também podem ser de outro servidor, contanto que você tenha nitro.\n" +
                                            "-# <a:critical29:1280914925727776801> **Aviso** <a:critical29:1280914925727776801>: Não coloquem emojis inapropriados. Os membros que fizerem isso estarão sujeitos a punições!",
                                    false
                            )
                            .addField(
                                    "`/moonvip editar`:",
                                    "Como o próprio comando sugere, ele é utilizado para editar o cargo criado anteriormente. " +
                                            "Nome, emoji e cor podem ser editados, da mesma forma que foi informado anteriormente.",
                                    false
                            )
                            .addField(
                                    "`/moonvip call`:",
                                    "Este comando serve para criar sua call. Para criá-la, você deve ter criado os cargos `moonvip`, e `moonfriend`, o qual está descrito abaixo. " +
                                            "Nesta call, apenas as pessoas que têm a sua tag compartilhada `moonfriend` possuem acesso.",
                                    false
                            )
                            .addField(
                                    "`/moonfriend criar`:",
                                    "Cria um cargo que pode ser compartilhado com amigos. Ele possui um limite de 8 pessoas, então não será possível dar este cargo para mais de 8 pessoas.",
                                    false
                            )
                            .addField(
                                    "`/moonfriend editar`:",
                                    "Edita o cargo criado anteriormente.",
                                    false
                            )
                            .addField(
                                    "`/moonfriend give`:",
                                    "Este comando serve para atribuir o cargo criado para a pessoa que desejar.",
                                    false
                            )
                            .addField(
                                    "`/moonfriend remove`:",
                                    "Este comando serve para remover o cargo que foi atribuído à pessoa desejada.",
                                    false
                            )
                            .setColor(minhacor);

                    event.getHook().sendMessageEmbeds(embed.build()).queue();
                }
            }
            else if(subcommand.equals("minguante"))
            {
                if(!hasMinguante)
                {
                    event.getHook().sendMessage("Você não tem permissão para usar este comando. Será necessário obter o **VIP Minguante**").queue();
                }
                else if(hasMinguante && !hasEclipseRole && !hasLuaCheiaRole)
                {
                    Color minhacor = new Color(0, 0, 0);
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**<<<<< Instruções de Comandos do VIP Minguante >>>>>**")
                            .setDescription(
                                    "Antes de explicar cada comando, será necessário utilizar algum site que forneça as cores em valores hexadecimal, seguido de `#`. " +
                                            "Vocês podem usar o site que preferirem, basta pesquisar `seletor de cores` no Google. Aqui está um site popular atualmente: \n\n" +
                                            "[Image Color Picker](https://imagecolorpicker.com/pt-pt)\n\n" +
                                            "Caso tenham alguma dúvida de como utilizar, vocês podem pedir ajuda aos staffs ou donos do servidor.\n\n"
                            )
                            .addField("", "**[Comandos](https://www)**", false)
                            .addField(
                                    "`/minguantevip criar`:",
                                    "Este comando serve para criar seu cargo único no servidor, onde ninguém mais terá acesso além de você. " +
                                            "Por favor, não peça para nenhum administrador do servidor atribuir este cargo a outra pessoa. " +
                                            "Os benefícios para este cargo são: nome, emoji e cor à sua escolha. Os emojis também podem ser de outro servidor, contanto que você tenha nitro.\n" +
                                            "-# <a:critical29:1280914925727776801> **Aviso** <a:critical29:1280914925727776801>: Não coloquem emojis inapropriados. Os membros que fizerem isso estarão sujeitos a punições!",
                                    false
                            )
                            .addField(
                                    "`/minguantevip editar`:",
                                    "Como o próprio comando sugere, ele é utilizado para editar o cargo criado anteriormente. " +
                                            "Nome, emoji e cor podem ser editados, da mesma forma que foi informado anteriormente.",
                                    false
                            )
                            .setColor(minhacor);

                    event.getHook().sendMessageEmbeds(embed.build()).queue();
                }
            }
        }

        else if(command.equals("criar"))
        {
            if(!hasEclipseRole)
            {
                event.getHook().sendMessage("Você não possui acesso a esse comando. Será necessário obter o **VIP Eclipse**").queue();
            }
            else if(hasEclipseRole && !hasLuaCheiaRole && !hasMinguante)
            {
                String subcommand = event.getSubcommandName();

                if(subcommand.equals("eclipsevip"))
                {
                    int temVip = hasVip(member.getId());
                    if(temVip == 0)
                    {
                        event.getHook().sendMessage("Você já possui vip. Não será possível criar outro cargo!").setEphemeral(true).queue();
                    }
                    else if(temVip == 1)
                    {
                        String cargo = event.getOption("cargo").getAsString();
                        List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                        String corCargo = event.getOption("cor").getAsString();

                        if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                        {
                            event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").setEphemeral(true).queue();
                            return;
                        }

                        if (emoji.size() != 1)
                        {
                            event.getHook().sendMessage("Insira um emoji válido").queue();
                            return;
                        }

                        final var emojiImage = emoji.get(0).getImage();
                        emojiImage.download().whenComplete((inputStream, exception) ->
                        {
                            if (exception != null)
                            {
                                //reply error
                                System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                            }
                            else
                            {
                                //Create the icon from the input stream
                                try
                                {
                                    // Criar um OutputStream para armazenar a imagem convertida
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                    // Converter a imagem de GIF para PNG
                                    boolean success = convertImg(inputStream, outputStream, "png");

                                    if (success)
                                    {
                                        System.out.println("Conversão concluída com sucesso.");
                                        // Aqui você pode usar o OutputStream como necessário
                                        // Converter o OutputStream para InputStream para criar o Icon
                                        InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                        // Criar o Icon a partir do InputStream
                                        Icon icon = Icon.from(pngInputStream);
                                        //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                        RoleAction roleAction = event.getGuild().createRole();
                                        roleAction.setName(cargo);
                                        roleAction.setColor(Color.decode(corCargo));
                                        roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                        roleAction.setIcon(icon).queue(role ->
                                        {

                                            // Cargo criado com sucesso
                                            String cargoMencionado = role.getAsMention();
                                            EmbedBuilder embedCriar = new EmbedBuilder()
                                                    .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                    .setTitle("<a:6954sylveonhappy:1228896788921192458> Criação de VIP <a:6954sylveonhappy:1228896788921192458>")
                                                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                    .addField("`Cargo`", cargoMencionado, true)
                                                    .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                    .setDescription("Parabéns! Seu VIP foi criado com sucesso! 🎉")
                                                    .setFooter("Sua experiência VIP está apenas começando! ✨", null)
                                                    .setImage("https://cdn.discordapp.com/attachments/1151196703475638373/1158065549331419206/png_20230606_154417_0000.png?ex=66423416&is=6640e296&hm=dc363a9c7b5bd160c4f698860d7dde9a2b5140c8529b402f59beb6d2e436a28d&")
                                                    .setColor(role.getColor());

                                            //tem que botar .queue aqui no debaixo, se não, não acontece nada
                                            guild.modifyRolePositions().selectPosition(role).moveBelow(event.getGuild().getRoleById("1224171011721924722")).queue();
                                            guild.addRoleToMember(member, role).queue();

                                            event.getHook().sendMessageEmbeds(embedCriar.build()).queue();

                                            String query = "update eclipsevip set idCustomRole = ?, nameCustomRole = ? where memberID = ?";
                                            PreparedStatement statement = null;

                                            try {
                                                statement = ConnectionDB.getConexao().prepareStatement(query);

                                                statement.setString(1, role.getId());
                                                statement.setString(2, role.getName());
                                                statement.setString(3, member.getId());

                                                int rowsAffected = statement.executeUpdate();

                                                if (rowsAffected > 0)
                                                {
                                                    System.out.println("Dados inseridos com sucesso!");
                                                }
                                                else
                                                {
                                                    System.out.println("Falha ao inserir dados do eclipsevip.");
                                                }

                                                statement.close();

                                            }
                                            catch (SQLException e)
                                            {
                                                // TODO: handle exception
                                            }

                                        });
                                    }

                                    else
                                    {
                                        System.out.println("Falha na conversão.");
                                    }
                                } catch (Exception e)
                                {
                                    // TODO: handle exception
                                }

                            }
                        });
                    }
                    else if(temVip == 2)
                    {
                        String cargo = event.getOption("cargo").getAsString();
                        List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                        String corCargo = event.getOption("cor").getAsString();

                        if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
                            event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                            return;
                        }

                        if (emoji.size() != 1)
                        {
                            event.getHook().sendMessage("Insira um emoji válido").setEphemeral(true).queue();
                            return;
                        }

                        final var emojiImage = emoji.get(0).getImage();
                        emojiImage.download().whenComplete((inputStream, exception) ->
                        {
                            if (exception != null)
                            {
                                //reply error
                                System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                            }
                            else
                            {
                                //Create the icon from the input stream
                                try
                                {
                                    // Criar um OutputStream para armazenar a imagem convertida
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                    // Converter a imagem de GIF para PNG
                                    boolean success = convertImg(inputStream, outputStream, "png");

                                    if (success)
                                    {
                                        System.out.println("Conversão concluída com sucesso.");
                                        // Aqui você pode usar o OutputStream como necessário
                                        // Converter o OutputStream para InputStream para criar o Icon
                                        InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                        // Criar o Icon a partir do InputStream
                                        Icon icon = Icon.from(pngInputStream);
                                        //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                        RoleAction roleAction = event.getGuild().createRole();
                                        roleAction.setName(cargo);
                                        roleAction.setColor(Color.decode(corCargo));
                                        roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                        roleAction.setIcon(icon).queue(role ->
                                        {

                                            // Cargo criado com sucesso
                                            String cargoMencionado = role.getAsMention();
                                            EmbedBuilder embedCriar = new EmbedBuilder()
                                                    .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                    .setTitle("<a:6954sylveonhappy:1228896788921192458> Criação de VIP <a:6954sylveonhappy:1228896788921192458>")
                                                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                    .addField("`Cargo`", cargoMencionado, true)
                                                    .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                    .setDescription("Parabéns! Seu VIP foi criado com sucesso! 🎉")
                                                    .setFooter("Sua experiência VIP está apenas começando! ✨", null)
                                                    .setImage("https://cdn.discordapp.com/attachments/1151196703475638373/1158065549331419206/png_20230606_154417_0000.png?ex=66423416&is=6640e296&hm=dc363a9c7b5bd160c4f698860d7dde9a2b5140c8529b402f59beb6d2e436a28d&")
                                                    .setColor(role.getColor());

                                            //tem que botar .queue aqui no debaixo, se não, não acontece nada

                                            guild.modifyRolePositions().selectPosition(role).moveBelow(event.getGuild().getRoleById("1224171011721924722")).queue();
                                            guild.addRoleToMember(member, role).queue();

                                            event.getHook().sendMessageEmbeds(embedCriar.build()).queue();

                                            String query = "INSERT INTO eclipsevip(memberID, nome, idVip, tipo, idCustomRole, nameCustomRole) VALUES (?, ?, ?, ?, ?, ?)";
                                            PreparedStatement statement = null;

                                            try
                                            {
                                                statement = ConnectionDB.getConexao().prepareStatement(query);

                                                statement.setString(1, member.getId());
                                                statement.setString(2, member.getEffectiveName());
                                                statement.setString(3, "1223689600321454153");
                                                statement.setString(4, "Eclipse");
                                                statement.setString(5, role.getId());
                                                statement.setString(6, role.getName());

                                                int rowsAffected = statement.executeUpdate();

                                                if (rowsAffected > 0)
                                                {
                                                    System.out.println("Dados inseridos com sucesso!");
                                                }
                                                else
                                                {
                                                    System.out.println("Falha ao inserir dados.");
                                                }

                                                statement.close();

                                            }
                                            catch (SQLException e)
                                            {
                                                // TODO: handle exception
                                            }

                                        });
                                    }
                                    else
                                    {
                                        System.out.println("Falha na conversão.");
                                    }
                                }
                                catch (Exception e)
                                {
                                    // TODO: handle exception
                                }

                            }
                        });

                    }
                    else if(temVip == 3)
                    {
                        event.getHook().sendMessage("Você já possui vip. Não será possível criar outro cargo!").setEphemeral(true).queue();
                    }
                }

                else if(subcommand.equals("eclipselover"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();
                    User user = event.getOption("nome").getAsUser();

                    if(member.getId().equals(user.getId()))
                    {
                        event.getHook().sendMessage("Você não pode atribuir o `eclipselover` em si mesmo. Por favor, atribua a outra pessoa").setEphemeral(true).queue();
                    }
                    else
                    {
                        try
                        {
                            String queryGetRole = "SELECT idCustomRole, loverIdRole FROM eclipsevip WHERE memberID = ? or loverId = ?";
                            PreparedStatement roleStatement = ConnectionDB.getConexao().prepareStatement(queryGetRole);
                            roleStatement.setString(1, member.getId());
                            roleStatement.setString(2, user.getId());

                            ResultSet roleResultSet = roleStatement.executeQuery();
                            String loverIdRole = null;
                            String getIdCustomRole = null;
                            if (roleResultSet.next())
                            {
                                loverIdRole = roleResultSet.getString("loverIdRole");
                            }

                            if (loverIdRole != null)
                            {
                                String query = "SELECT memberID, loverId, loverIdRole FROM eclipsevip WHERE (memberID = ? or loverId = ?) and loverIdRole = ?";
                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query);
                                statement2.setString(1, member.getId());
                                statement2.setString(2, user.getId());
                                statement2.setString(3, loverIdRole);

                                ResultSet resultSet2 = statement2.executeQuery(); // Executa a consulta e obtém o resultado
                                // Continue aqui com o processamento do ResultSet
                                if(resultSet2.next())
                                {
                                    event.getHook().sendMessage("Você só pode dar o cargo para uma pessoa ou atribuí-lo apenas uma vez a alguém!").setEphemeral(true).queue();
                                }

                            }
                            else
                            {
                                // Trate o caso em que loverIdRole não foi encontrado para os IDs fornecidos
                                if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                {
                                    event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").setEphemeral(true).queue();
                                    return;
                                }

                                if (emoji.size() != 1)
                                {
                                    event.getHook().sendMessage("Insira um emoji válido").setEphemeral(true).queue();
                                    return;
                                }

                                final var emojiImage = emoji.get(0).getImage();
                                emojiImage.download().whenComplete((inputStream, exception) ->
                                {
                                    if (exception != null)
                                    {
                                        //reply error
                                        System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                    }
                                    else
                                    {
                                        //Create the icon from the input stream
                                        try
                                        {
                                            // Criar um OutputStream para armazenar a imagem convertida
                                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                            // Converter a imagem de GIF para PNG
                                            boolean success = convertImg(inputStream, outputStream, "png");

                                            if (success)
                                            {
                                                System.out.println("Conversão concluída com sucesso.");
                                                // Aqui você pode usar o OutputStream como necessário
                                                // Converter o OutputStream para InputStream para criar o Icon
                                                InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                                // Criar o Icon a partir do InputStream
                                                Icon icon = Icon.from(pngInputStream);
                                                //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                                RoleAction roleAction = event.getGuild().createRole();
                                                roleAction.setName(cargo);
                                                roleAction.setColor(Color.decode(corCargo));
                                                roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                                roleAction.setIcon(icon).queue(role ->
                                                {

                                                    // Cargo criado com sucesso
                                                    String cargoMencionado = role.getAsMention();
                                                    EmbedBuilder embedCriar = new EmbedBuilder()
                                                            .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                            .setTitle("<a:6954sylveonhappy:1228896788921192458> Criação de Eclipse Lover <a:6954sylveonhappy:1228896788921192458>")
                                                            .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                            .addField("`Cargo`", cargoMencionado, true)
                                                            .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                            .addField("`Lover`", user.getAsMention(), true)
                                                            .setDescription("Parabéns! Seu Eclipse Lover foi criado com sucesso! 🎉")
                                                            .setFooter("Sua experiência VIP está apenas começando! ✨", null)
                                                            .setImage("https://cdn.discordapp.com/attachments/1151196703475638373/1175693456677539902/5EEB1AA6-B569-43A2-853C-5570A8E9FBD6.gif?ex=6641bbdb&is=66406a5b&hm=ed61b4ca4dee6807c99d9601dd1a8416996e0b8a6e06818031ff4e9178000266&")
                                                            .setColor(role.getColor());

                                                    event.getHook().sendMessageEmbeds(embedCriar.build()).queue();
                                                    //tem que botar .queue aqui no debaixo, se não, não acontece nada
                                                    guild.modifyRolePositions().selectPosition(role).moveAbove(event.getGuild().getRoleById("1223767698106748983")).queue();
                                                    guild.addRoleToMember(member, role).queue();
                                                    guild.addRoleToMember(user, role).queue(); // Encadeia a segunda ação

                                                    String query = "select idCustomRole from eclipsevip where memberID = ?";
                                                    PreparedStatement statement = null;

                                                    try
                                                    {
                                                        statement = ConnectionDB.getConexao().prepareStatement(query);
                                                        statement.setString(1, member.getId());

                                                        ResultSet resultSet = statement.executeQuery();

                                                        if (resultSet.next())
                                                        {
                                                            String query2 = "update eclipsevip set loverId = ?, loverName = ?, loverIdRole = ?, loverRoleName = ? where memberID = ?";
                                                            PreparedStatement statement2 = null;

                                                            try {
                                                                statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                                                statement2.setString(1, user.getId());
                                                                statement2.setString(2, user.getEffectiveName());
                                                                statement2.setString(3, role.getId());
                                                                statement2.setString(4, role.getName());
                                                                statement2.setString(5, member.getId());

                                                                int rowsAffected = statement2.executeUpdate();

                                                                if (rowsAffected > 0)
                                                                {
                                                                    System.out.println("Dados atualizados com sucesso do Lover!");
                                                                }
                                                                else
                                                                {
                                                                    System.out.println("Falha ao atualizar dados do Lover!");
                                                                }

                                                            }
                                                            catch (SQLException e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            String query2 = "insert into eclipsevip(nome, memberID, idVip, tipo, loverId, loverName, loverIdRole, loverRoleName) values(?, ?, ?, ?, ?, ?, ?, ?)";
                                                            PreparedStatement statement2 = null;

                                                            try
                                                            {
                                                                statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                                                statement2.setString(1, member.getEffectiveName());
                                                                statement2.setString(2, member.getId());
                                                                statement2.setString(3, eclipse.getId());
                                                                statement2.setString(4, "Eclipse");
                                                                statement2.setString(5, user.getId());
                                                                statement2.setString(6, user.getEffectiveName());
                                                                statement2.setString(7, role.getId());
                                                                statement2.setString(8, role.getName());

                                                                int rowsAffected = statement2.executeUpdate();

                                                                if (rowsAffected > 0)
                                                                {
                                                                    System.out.println("Dados inseridos com sucesso do Lover!");
                                                                }
                                                                else
                                                                {
                                                                    System.out.println("Falha ao inserir dados do Lover!");
                                                                }

                                                            }
                                                            catch (SQLException e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                    } catch (SQLException e) {
                                                        // TODO: handle exception
                                                    }

                                                });
                                            }

                                            else
                                            {
                                                System.out.println("Falha na conversão.");
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            // TODO: handle exception
                                        }

                                    }
                                });
                            }


                        }
                        catch (SQLException e)
                        {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }
                else if(subcommand.equals("eclipsefriend"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    //verificar se usou eclipse lover ou eclipsefriend
                    try
                    {
                        String query = "select idCustomRole, loverIdRole from eclipsevip where memberID = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        String getidCustomRole = null;
                        String getloverIdRole;
                        if(resultset.next())//significa que eu fui registrado na tabela por ter executado algum comando dos vips, no caso eclipsevip ou eclipselover
                        {
                            //agora, eu preciso ver se eu já criei o cargo. Se não criei, crio. Se criei, falo que não pode mais criar
                            getidCustomRole = resultset.getString("idCustomRole");
                            getloverIdRole = resultset.getString("loverIdRole");

                            if(getidCustomRole == null && getloverIdRole != null)
                            {
                                event.getHook().sendMessage("Você ainda não usou o comando `/criar eclipsevip`!").setEphemeral(true).queue();
                            }
                            else if(getidCustomRole != null && getloverIdRole == null)
                            {
                                event.getHook().sendMessage("Você ainda não usou o comando `/criar eclipselover`!").setEphemeral(true).queue();
                            }
                            else if(getidCustomRole != null && getloverIdRole != null)
                            {
                                try
                                {
                                    //eu só posso criar o cargo se eu já tiver usado o comando, mas a pessoa com a tag pode dar esse comando
                                    String query2 = "select createdFriendRoleID from eclipsefriend where memberID = ?";
                                    PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                    statement2.setString(1, member.getId());
                                    ResultSet resultset2 = statement2.executeQuery();
                                    if(resultset2.next() == true)
                                    {
                                        event.getHook().sendMessage("Você já criou o cargo, não pode mais criar outro. Caso queira editar o cargo, digite `/editar eclipsefriend`").setEphemeral(true).queue();
                                    }
                                    else
                                    {
                                        if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                        {
                                            event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").setEphemeral(true).queue();
                                            return;
                                        }

                                        if (emoji.size() != 1)
                                        {
                                            event.getHook().sendMessage("Insira um emoji válido").setEphemeral(true).queue();
                                            return;
                                        }

                                        final var emojiImage = emoji.get(0).getImage();
                                        emojiImage.download().whenComplete((inputStream, exception) ->
                                        {
                                            if (exception != null)
                                            {
                                                //reply error
                                                System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                            }
                                            else
                                            {
                                                //Create the icon from the input stream
                                                try
                                                {
                                                    // Criar um OutputStream para armazenar a imagem convertida
                                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                                    // Converter a imagem de GIF para PNG
                                                    boolean success = convertImg(inputStream, outputStream, "png");

                                                    if (success)
                                                    {
                                                        System.out.println("Conversão concluída com sucesso.");
                                                        // Aqui você pode usar o OutputStream como necessário
                                                        // Converter o OutputStream para InputStream para criar o Icon
                                                        InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                                        // Criar o Icon a partir do InputStream
                                                        Icon icon = Icon.from(pngInputStream);
                                                        //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                                        RoleAction roleAction = event.getGuild().createRole();
                                                        roleAction.setName(cargo);
                                                        roleAction.setColor(Color.decode(corCargo));
                                                        roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                                        roleAction.setIcon(icon).queue(role ->
                                                        {

                                                            // Cargo criado com sucesso
                                                            String cargoMencionado = role.getAsMention();
                                                            EmbedBuilder embedCriar = new EmbedBuilder()
                                                                    .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                                    .setTitle("<a:6954sylveonhappy:1228896788921192458> Eclipse Friend <a:6954sylveonhappy:1228896788921192458>")
                                                                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                                    .addField("`Cargo Compartilhado`", cargoMencionado, true)
                                                                    .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                                    .setDescription("Cargo Eclipse Friend criado com sucesso, aproveite!")
                                                                    .setFooter("Aproveite o seu cargo!", null)
                                                                    .setImage("https://media.discordapp.net/attachments/1239099776876806204/1244987347909742633/image0-28-1-3-1.gif?ex=665bb992&is=665a6812&hm=085998c8d85fbb07c99cd976e7eaae3eaa2b54d4e864df72f2f407544169de21&=")
                                                                    .setColor(role.getColor());

                                                            //tem que botar .queue aqui no debaixo, se não, não acontece nada
                                                            guild.modifyRolePositions().selectPosition(role).moveAbove(event.getGuild().getRoleById("1223412632850141237")).queue();
                                                            guild.addRoleToMember(member, role).queue();

                                                            event.getHook().sendMessageEmbeds(embedCriar.build()).queue();


                                                            String query3 = "INSERT INTO eclipsefriend(memberID, nome, createdFriendRoleID, createdFriendRoleName) VALUES (?, ?, ?, ?)";

                                                            try
                                                            {
                                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                                statement3.setString(1, member.getId());
                                                                statement3.setString(2, member.getEffectiveName());
                                                                statement3.setString(3, role.getId());
                                                                statement3.setString(4, role.getName());

                                                                int rowsAffected2 = statement3.executeUpdate();

                                                                if (rowsAffected2 > 0)
                                                                {
                                                                    System.out.println("Dados inseridos com sucesso!");
                                                                }
                                                                else
                                                                {
                                                                    System.out.println("Falha ao inserir dados.");
                                                                }

                                                            }
                                                            catch (SQLException e)
                                                            {
                                                                // TODO: handle exception
                                                            }

                                                        });
                                                    }
                                                    else
                                                    {
                                                        System.out.println("Falha na conversão.");
                                                    }
                                                }
                                                catch (Exception e)
                                                {
                                                    // TODO: handle exception
                                                }

                                            }
                                        });
                                    }

                                }
                                catch(SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }

                        else
                        {
                            event.getHook().sendMessage("Você ainda não utilizou nenhum comando do VIP. Utilize `/criar eclipsevip` e `/criar eclipselover` para usar este comando!").setEphemeral(true).queue();
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }

                else if(subcommand.equals("eclipsecall"))
                {
                    System.out.println("meu Deus");
                    try
                    {
                        String query = "select idCustomRole, callID from eclipsevip where memberID = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                        statement.setString(1, member.getId());
                        System.out.println("meu senhor");
                        ResultSet resultset = statement.executeQuery();

                        String getidCustomRole = null;
                        String getcallID = null;
                        if(resultset.next())
                        {
                            System.out.println("me ajuda por favor");
                            getidCustomRole = resultset.getString("idCustomRole");
                            getcallID = resultset.getString("callID");
                            if(getidCustomRole != null && getcallID != null)
                            {
                                event.getHook().sendMessage("Você já criou sua call, não será possível criar outra!").setEphemeral(true).queue();
                            }
                            else if(getidCustomRole != null && getcallID == null)
                            {
                                try
                                {
                                    String query3 = "select createdFriendRoleID from eclipsefriend where memberID = ?";
                                    PreparedStatement  statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                    statement3.setString(1, member.getId());
                                    ResultSet resultset3 = statement3.executeQuery();
                                    if(resultset3.next())
                                    {
                                        String callEclipse = event.getOption("nome").getAsString();
                                        String getcreatedFriendRoleID = resultset3.getString("createdFriendRoleID");

                                        event.getGuild().createVoiceChannel("Canal de voz")
                                                .setName(callEclipse)
                                                .setParent(eclipseCattegory)
                                                .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_START_ACTIVITIES, Permission.VOICE_STREAM, Permission.VOICE_SET_STATUS), Collections.emptyList())  // Allow specific permissions to member
                                                .addPermissionOverride(event.getGuild().getRoleById(getcreatedFriendRoleID), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_START_ACTIVITIES, Permission.VOICE_STREAM), Collections.emptyList())  // Allow specific permissions to staff role
                                                .addPermissionOverride(guild.getPublicRole(), EnumSet.of(Permission.VIEW_CHANNEL), EnumSet.of(Permission.VOICE_CONNECT))  // Nega permissões para o everyone
                                                .queue(channel ->
                                                        {
                                                            event.getHook().sendMessage("O canal de voz " + "`" + channel.getName() + "`" + " foi criado em <#" + channel.getId() + ">").queue();

                                                            try
                                                            {
                                                                String query2 = "update eclipsevip set callName = ?, callID = ? where memberID = ?";
                                                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                                                statement2.setString(1, channel.getName());
                                                                statement2.setString(2, channel.getId());
                                                                statement2.setString(3, member.getId());

                                                                int rowsAffected = statement2.executeUpdate();

                                                                if(rowsAffected > 0)
                                                                {
                                                                    System.out.println("Dados da call inseridas com sucesso!");
                                                                }
                                                                else
                                                                {
                                                                    System.out.println("Ocorreu um erro ao inserir os dados da call!");
                                                                }
                                                            }
                                                            catch(SQLException e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        },
                                                        error ->
                                                        {
                                                            event.getHook().sendMessage("Ocorreu um erro ao criar o canal de voz: " + error.getMessage()).queue();
                                                        });
                                    }
                                    else
                                    {
                                        System.out.println("Para criar a call, crie o cargo de **ECLIPSE FRIEND** com o comando `/criar eclipsefriend`");
                                        event.getHook().sendMessage("Para criar a call, crie o cargo de **ECLIPSE FRIEND** com o comando `/criar eclipsefriend`").queue();
                                    }
                                }
                                catch(SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            System.out.println("Você ainda não utilizou um comando do VIP. Digite `/criar eclipsevip` ou `/criar eclipselover` para utilizar este comando!");
                            event.getHook().sendMessage("Você ainda não utilizou um comando do VIP. Digite `/criar eclipsevip` ou `/criar eclipselover` para utilizar este comando!").queue();
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        else if(command.equals("editar"))
        {
            if(!hasEclipseRole)
            {
                event.getHook().sendMessage("Você não possui acesso a esse comando. Será necessário obter o **VIP Eclipse**").queue();
            }
            else if(hasEclipseRole && !hasLuaCheiaRole && !hasMinguante)
            {
                String subcommand = event.getSubcommandName();

                if(subcommand.equals("eclipsevip"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    String query = "select * from eclipsevip where memberID = ?";
                    try
                    {
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        if(resultset.next())
                        {
                            try
                            {
                                String query2 = "SELECT idCustomRole FROM eclipsevip WHERE memberID = ?";

                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                                statement2.setString(1, member.getId());

                                ResultSet resultset2 = statement2.executeQuery();

                                String getquery = null;

                                if(resultset2.next())
                                {
                                    getquery = resultset2.getString("idCustomRole");
                                }

                                if(getquery != null)
                                {
                                    Role role = event.getGuild().getRoleById(getquery);
                                    // Trate o caso em que loverIdRole não foi encontrado para os IDs fornecidos
                                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                    {
                                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                                        return;
                                    }

                                    if (emoji.size() != 1) {
                                        event.getHook().sendMessage("Insira um emoji válido").queue();
                                        return;
                                    }

                                    final var emojiImage = emoji.get(0).getImage();
                                    emojiImage.download().whenComplete((inputStream, exception) ->
                                    {
                                        if (exception != null)
                                        {
                                            //reply error
                                            System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                        }
                                        else
                                        {
                                            //Create the icon from the input stream
                                            try
                                            {
                                                // Criar um OutputStream para armazenar a imagem convertida
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                                // Converter a imagem de GIF para PNG
                                                boolean success = convertImg(inputStream, outputStream, "png");

                                                if (success)
                                                {
                                                    System.out.println("Conversão concluída com sucesso.");
                                                    // Aqui você pode usar o OutputStream como necessário
                                                    // Converter o OutputStream para InputStream para criar o Icon
                                                    InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                                    // Criar o Icon a partir do InputStream
                                                    Icon icon = Icon.from(pngInputStream);

                                                    // Use o Icon para definir o ícone do cargo no Discord
                                                    role.getManager()
                                                            .setName(cargo)
                                                            .setColor(Color.decode(corCargo))
                                                            .setPermissions(Collections.singletonList(Permission.MESSAGE_SEND))
                                                            .setIcon(icon).queue();
                                                }
                                                else
                                                {
                                                    System.out.println("Falha na conversão.");
                                                }



                                                // Cargo criado com sucesso
                                                String cargoMencionado = role.getAsMention();
                                                EmbedBuilder embedCriar = new EmbedBuilder()
                                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                        .setTitle("<a:6954sylveonhappy:1228896788921192458> Edição de VIP <a:6954sylveonhappy:1228896788921192458>")
                                                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                        .addField("`Novo cargo`", cargoMencionado, true)
                                                        .addField("`Novo emoji`", emoji.get(0).getAsMention(), true)
                                                        .setDescription("Seu cargo foi editado com sucesso! 🎉")
                                                        .setImage("https://cdn.discordapp.com/attachments/1151196703475638373/1158065549331419206/png_20230606_154417_0000.png?ex=66423416&is=6640e296&hm=dc363a9c7b5bd160c4f698860d7dde9a2b5140c8529b402f59beb6d2e436a28d&")
                                                        .setColor(role.getColor());

                                                event.getHook().sendMessageEmbeds(embedCriar.build()).queue();
                                                //tem que botar .queue aqui no debaixo, se não, não acontece nada

                                                try
                                                {
                                                    String query3 = "update eclipsevip set nameCustomRole = ? where idCustomRole = ?";
                                                    PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                    statement3.setString(1, cargo);
                                                    statement3.setString(2, role.getId());

                                                    int rowsAffected = statement3.executeUpdate();

                                                    if(rowsAffected > 0)
                                                    {
                                                        System.out.println("Cargo atualizado com sucesso");
                                                    }
                                                    else
                                                    {
                                                        System.out.println("Não teve sucesso ao atualizar os cargos");
                                                    }
                                                }
                                                catch (SQLException e)
                                                {
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch(IOException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                                else
                                {
                                    event.getHook().sendMessage("Para editar seu cargo, você deve criar o próprio cargo em `/criar eclipsevip`!").queue();
                                }


                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }

                        }

                        else
                        {
                            event.getHook().sendMessage("Seu **VIP** ainda não está registrado. Use o comando `/criar eclipsevip` ou `/criar eclipselover` para se registrar!").queue();;
                        }
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }

                else if(subcommand.equals("eclipselover"))
                {
                    //esse comando é para editar o cargo. Então eu vou editar o nome, cor, e emoji. Também posso mudar o cargo de pessoa
                    //posso tirar o cargo de uma pessoa para dar para outra
                    //se quando eu estiver usando o comando e botar o nome da pessoa que já tem o cargo, eu só atualizo o cargo(nome, emoji e cor)
                /*se for uma pessoa diferente, eu faço tudo isso, atualizo no banco de dados o novo lover, remove o cargo do último lover e
                bota no outro*/

                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();
                    User user = event.getOption("nome").getAsUser();

                    if(member.getId().equals(user.getId()))
                    {
                        event.getHook().sendMessage("Você não pode atribuir o cargo `eclipselover` em si mesmo. Por favor, atribua a outra pessoa").queue();
                    }

                    else
                    {
                        //primeiro, vamos procurar no banco de dados para ver se a pessoa que usou o comando já tem eclipselover setado
                        try
                        {
                            String query = "select loverId, loverIdRole, loverName from eclipsevip where memberID = ?";
                            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                            statement.setString(1, member.getId());

                            ResultSet resultset = statement.executeQuery();

                            String loverId = null;
                            String loverIdRole = null;
                            if(resultset.next())
                            {
                                //se retorna null, quer dizer que eu não possui eclipselover ainda
                                loverId = resultset.getString("loverId");
                                loverIdRole = resultset.getString("loverIdRole");

                                if(loverId != null && loverIdRole != null) //significa que eu achei na tabela e, portanto, eu já teria usado eclipselover
                                {
                                    //vamos atualizar o cargo, de qualquer forma
                                    Role role = event.getGuild().getRoleById(loverIdRole); //cargo do banco de dados
                                    Member member2 = event.getGuild().getMemberById(loverId); //id do lover do banco de dados

                                    //configurando a cor para hexadecimal
                                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                    {
                                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                                        return;
                                    }

                                    if (emoji.size() != 1)
                                    {
                                        event.getHook().sendMessage("Insira um emoji válido").queue();
                                        return;
                                    }

                                    final var emojiImage = emoji.get(0).getImage();
                                    emojiImage.download().whenComplete((inputStream, exception) ->
                                    {
                                        if (exception != null)
                                        {
                                            System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                            exception.printStackTrace();
                                        }
                                        else
                                        {
                                            try
                                            {
                                                System.out.println("Iniciando conversão de imagem.");
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                                boolean success = convertImg(inputStream, outputStream, "png");

                                                if (success)
                                                {
                                                    System.out.println("Conversão concluída com sucesso.");
                                                    InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());
                                                    Icon icon = Icon.from(pngInputStream);

                                                    System.out.println("Atualizando o cargo com o novo ícone.");
                                                    role.getManager()
                                                            .setName(cargo)
                                                            .setColor(Color.decode(corCargo))
                                                            .setPermissions(Collections.singletonList(Permission.MESSAGE_SEND))
                                                            .setIcon(icon).queue();

                                                    System.out.println("Removendo cargo antigo e atribuindo ao novo lover.");
                                                    if(member2 != null)
                                                    {
                                                        event.getGuild().removeRoleFromMember(member2, role).queue();
                                                        event.getGuild().addRoleToMember(user, role).queue();
                                                    }
                                                    else if(member2 == null)
                                                    {
                                                        event.getGuild().addRoleToMember(user, role).queue();
                                                        System.out.println("Membro não encontado");
                                                    }

                                                    // Cargo criado com sucesso
                                                    String cargoMencionado = role.getAsMention();
                                                    EmbedBuilder embedCriar = new EmbedBuilder()
                                                            .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                            .setTitle("<a:6954sylveonhappy:1228896788921192458> Edição de Eclipse Lover <a:6954sylveonhappy:1228896788921192458>")
                                                            .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                            .addField("`Novo Cargo`", cargoMencionado, true)
                                                            .addField("`Novo Emoji`", emoji.get(0).getAsMention(), true)
                                                            .addField("`Novo Lover`", user.getAsMention(), true)
                                                            .setDescription("Eclipse lover editado com sucesso! 🎉")
                                                            .setImage("https://cdn.discordapp.com/attachments/1151196703475638373/1175693456677539902/5EEB1AA6-B569-43A2-853C-5570A8E9FBD6.gif?ex=6641bbdb&is=66406a5b&hm=ed61b4ca4dee6807c99d9601dd1a8416996e0b8a6e06818031ff4e9178000266&")
                                                            .setColor(role.getColor());

                                                    event.getHook().sendMessageEmbeds(embedCriar.build()).queue();

                                                    try
                                                    {
                                                        System.out.println("Atualizando o banco de dados.");
                                                        String query2 = "update eclipsevip set loverId = ?, loverName = ?, loverRoleName = ? where memberID = ?";
                                                        PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                                                        statement2.setString(1, user.getId());
                                                        statement2.setString(2, user.getEffectiveName());
                                                        statement2.setString(3, cargo);
                                                        statement2.setString(4, member.getId());

                                                        int rowsAffected = statement2.executeUpdate();
                                                        if (rowsAffected > 0)
                                                        {
                                                            System.out.println("Lover atualizado com sucesso!");
                                                        }
                                                        else
                                                        {
                                                            System.out.println("Deu problema ao atualizar o lover!");
                                                        }
                                                    }
                                                    catch (SQLException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else
                                                {
                                                    System.out.println("Falha na conversão.");
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                }
                                else //aqui é quando eu não tenho um eclipselover setado ainda
                                {
                                    event.getHook().sendMessage("Você ainda não utilizou o comando `/criar eclipselover`!").queue();
                                }
                            }
                            else //aqui tem a possibilidade de eu não estar registrado no banco de dados por não ter usado nenhum comando
                            {
                                event.getHook().sendMessage("Você ainda não usou nenhum comando do VIP. Por favor, utilize `/criar eclipsevip` ou `/criar eclipselover`!").queue();
                            }
                        }
                        catch (Exception e)
                        {
                            // TODO: handle exception
                        }
                    }
                }

                else if (subcommand.equals("eclipsefriend"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    try
                    {
                        //para dar o cargo para alguém, tenho que ter criado o cargo primeiro, então preciso verificar no banco de dados
                        String query = "select friendID, createdFriendRoleID from eclipsefriend where memberID = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        String getFriendRoleID = null;
                        String getfriendID = null;
                        //retorna true se o membro já estiver registrado no banco de dados, que significa que já criei o cargo
                        if(resultset.next())
                        {
                            getFriendRoleID = resultset.getString("createdFriendRoleID");
                            getfriendID = resultset.getString("friendID");

                            Role role = event.getGuild().getRoleById(getFriendRoleID);
                            Member member2 = event.getMember();

                            // ---------------------------------------------------------------------------------------
                            // Define o formato da data e hora
                            SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                            formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                            // Obtém a data e hora atual e formata
                            Date data = new Date();
                            String dataFormatada = formatar.format(data);

                            // ---------------------------------------------------------------------------------------
                            if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                            {
                                event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                                return;
                            }

                            if (emoji.size() != 1)
                            {
                                event.getHook().sendMessage("Insira um emoji válido").queue();
                                return;
                            }

                            final var emojiImage = emoji.get(0).getImage();
                            emojiImage.download().whenComplete((inputStream, exception) ->
                            {
                                if (exception != null)
                                {
                                    //reply error
                                    System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                }
                                else
                                {
                                    //Create the icon from the input stream
                                    try
                                    {
                                        // Criar um OutputStream para armazenar a imagem convertida
                                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                        // Converter a imagem de GIF para PNG
                                        boolean success = convertImg(inputStream, outputStream, "png");

                                        if (success)
                                        {
                                            System.out.println("Conversão concluída com sucesso.");
                                            // Aqui você pode usar o OutputStream como necessário
                                            // Converter o OutputStream para InputStream para criar o Icon
                                            InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                            // Criar o Icon a partir do InputStream
                                            Icon icon = Icon.from(pngInputStream);
                                            //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                            role.getManager()
                                                    .setName(cargo)
                                                    .setColor(Color.decode(corCargo))
                                                    .setPermissions(Collections.singletonList(Permission.MESSAGE_SEND))
                                                    .setIcon(icon).queue();

                                            // Cargo criado com sucesso
                                            String cargoMencionado = role.getAsMention();
                                            EmbedBuilder embedCriar = new EmbedBuilder()
                                                    .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                    .setTitle("<a:6954sylveonhappy:1228896788921192458> Edição de VIP <a:6954sylveonhappy:1228896788921192458>")
                                                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                    .addField("`Novo cargo`", cargoMencionado, true)
                                                    .addField("`Novo emoji`", emoji.get(0).getAsMention(), true)
                                                    .setDescription("Seu cargo foi editado com sucesso! 🎉")
                                                    .setImage("https://cdn.discordapp.com/attachments/1151196703475638373/1158065549331419206/png_20230606_154417_0000.png?ex=66423416&is=6640e296&hm=dc363a9c7b5bd160c4f698860d7dde9a2b5140c8529b402f59beb6d2e436a28d&")
                                                    .setColor(role.getColor());

                                            event.getHook().sendMessageEmbeds(embedCriar.build()).queue();
                                            //tem que botar .queue aqui no debaixo, se não, não acontece nada

                                            try
                                            {
                                                String query3 = "update eclipsefriend set createdFriendRoleName = ? where memberID = ? or friendID = ?";
                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                statement3.setString(1, cargo);
                                                statement3.setString(2, member.getId());
                                                statement3.setString(3, member2.getId());

                                                int rowsAffected = statement3.executeUpdate();

                                                if (rowsAffected > 0)
                                                {
                                                    System.out.println("Cargo atualizado com sucesso");
                                                }
                                                else
                                                {
                                                    System.out.println("Não teve sucesso ao atualizar os cargos");
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("Falha na conversão.");
                                        }
                                    }
                                    catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                        else
                        {
                            event.getHook().sendMessage("Para dar o cargo, você precisa criá-lo primeiro. Utilize o comando `/criar eclipsefriend`").queue();
                        }

                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        else if(command.equals("give"))
        {
            if(!hasEclipseRole)
            {
                event.getHook().sendMessage("Você não possui acesso a esse comando. Será necessário obter o **VIP Eclipse**").queue();
            }
            else if(hasEclipseRole && !hasLuaCheiaRole && !hasMinguante)
            {
                String subcommand = event.getSubcommandName();

                if(subcommand.equals("eclipsefriend"))
                {
                    User user = event.getOption("nome").getAsUser();

                    if(member.getId().equals(user.getId()))
                    {
                        event.getHook().sendMessage("Você já possui o cargo, só pode dar ele para outro membro do servidor").queue();
                    }
                    else
                    {
                        try
                        {
                            //para dar o cargo para alguém, tenho que ter criado o cargo primeiro, então preciso verificar no banco de dados
                            String query = "select friendID, createdFriendRoleID, removed from eclipsefriend where memberID = ?";
                            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                            statement.setString(1, member.getId());

                            ResultSet resultset = statement.executeQuery();

                            String getFriendRoleID = null;
                            String getfriendID = null;
                            String cargoremovido = null;
                            //retorna true se o membro já estiver registrado no banco de dados, que significa que já criei o cargo
                            if(resultset.next())
                            {
                                try
                                {
                                    String querychan = "SELECT COUNT(*) AS count FROM eclipsefriend WHERE memberID = ? AND (removed IS NULL OR removed = '0')";

                                    PreparedStatement statement6 = ConnectionDB.getConexao().prepareStatement(querychan);

                                    statement6.setString(1, member.getId());

                                    ResultSet resultset6 = statement6.executeQuery();

                                    int limite = 0;
                                    if(resultset6.next())
                                    {
                                        limite = resultset6.getInt("count");
                                    }

                                    cargoremovido = resultset.getString("removed");
                                    if(limite >= 0 && limite != 12)
                                    {
                                        System.out.println(member.getEffectiveName() + "Deu o cargo para " + limite + " pessoas!");
                                        getfriendID = resultset.getString("friendID");
                                        getFriendRoleID = resultset.getString("createdFriendRoleID");

                                        Role role = event.getGuild().getRoleById(getFriendRoleID);

                                        // ---------------------------------------------------------------------------------------
                                        // Define o formato da data e hora
                                        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                        formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                                        // Obtém a data e hora atual e formata
                                        Date data = new Date();
                                        String dataFormatada = formatar.format(data);

                                        // ---------------------------------------------------------------------------------------

                                        if(getfriendID == null) //significa que não deu o cargo pra ninguém, entao faço um update
                                        {
                                            try
                                            {
                                                String query2 = "update eclipsefriend set friendName = ?, friendID = ? where memberID = ?";
                                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                                statement2.setString(1, user.getEffectiveName());
                                                statement2.setString(2, user.getId());
                                                statement2.setString(3, member.getId());

                                                int rowsAffected = statement2.executeUpdate();

                                                if(rowsAffected > 0)
                                                {
                                                    System.out.println("eclipsefriend inserido com sucesso!");

                                                    event.getGuild().addRoleToMember(user, role).queue();
                                                    EmbedBuilder embed = new EmbedBuilder();
                                                    embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                                    embed.setTitle("Cargo Atribuído!");
                                                    embed.setThumbnail(user.getAvatarUrl());
                                                    embed.setDescription("Um novo cargo foi atribuído a um membro.");
                                                    embed.addField("Membro:", user.getAsMention(), false);
                                                    embed.addField("Cargo:", role.getAsMention(), false);
                                                    embed.setImage("https://imgur.com/hxT05ye.gif");
                                                    embed.setFooter(dataFormatada, event.getGuild().getIconUrl());

                                                    event.getHook().sendMessageEmbeds(embed.build()).queue();
                                                }
                                                else
                                                {
                                                    System.out.printf("Ocorreu um erro ao inserir dados no elcipsefriend!");
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }

                                        else if(getfriendID != null) //signfica que já dei cargo para alguem
                                        {
                                            //primeiro, vamos ver se eu não estou inserindo a mesma pessoa duas vezes
                                            try
                                            {
                                                String query3 = "select removed, friendID from eclipsefriend where memberID = ? and friendID = ?";
                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                statement3.setString(1, member.getId());
                                                statement3.setString(2, user.getId());
                                                ResultSet resultset3 = statement3.executeQuery();

                                                String getremoved = resultset3.getString("removed");
                                                if (resultset3.next()) //estou tentando dar o cargo ao mesmo membro
                                                {
                                                    if (getremoved == null || getremoved.equals("0"))
                                                    {
                                                        event.getHook().sendMessage("Você não pode dar o cargo pra mesma pessoa duas vezes!").queue();
                                                    }

                                                    else if(getremoved.equals("1"))
                                                    {
                                                        try
                                                        {
                                                            System.out.println("Cargo dado a um novo membro e foi inserido com sucesso!");

                                                            event.getGuild().addRoleToMember(user, role).queue();
                                                            EmbedBuilder embed = new EmbedBuilder();
                                                            embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                                            embed.setTitle("Cargo Atribuído!");
                                                            embed.setThumbnail(user.getAvatarUrl());
                                                            embed.setDescription("Um novo cargo foi atribuído a um membro.");
                                                            embed.addField("Membro:", user.getAsMention(), false);
                                                            embed.addField("Cargo:", role.getAsMention(), false);
                                                            embed.setImage("https://imgur.com/hxT05ye.gif");
                                                            embed.setFooter(dataFormatada, event.getGuild().getIconUrl());

                                                            event.getHook().sendMessageEmbeds(embed.build()).queue();

                                                            String query4 = "update eclipsefriend set removed = false where memberID = ? and friendID = ?";
                                                            PreparedStatement statement4 = ConnectionDB.getConexao().prepareStatement(query4);

                                                            statement4.setString(1, member.getId());
                                                            statement4.setString(2, user.getId());

                                                            int rowsAffected = statement4.executeUpdate();

                                                            if(rowsAffected > 0)
                                                            {
                                                                System.out.println("Exito ao atualizar o removed");

                                                                event.reply("Você não pode dar o cargo pra mesma pessoa duas vezes!").queue();
                                                            }
                                                            else
                                                            {
                                                                System.out.println("Não deu certo de atualizar o removed");
                                                            }
                                                        }
                                                        catch(SQLException e)
                                                        {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }
                                                else
                                                {
                                                    String query4 = "INSERT INTO eclipsefriend(memberID, nome, friendName, friendID, createdFriendRoleID, createdFriendRoleName) VALUES (?, ?, ?, ?, ?, ?)";

                                                    try
                                                    {
                                                        PreparedStatement statement4 = ConnectionDB.getConexao().prepareStatement(query4);

                                                        statement4.setString(1, member.getId());
                                                        statement4.setString(2, member.getEffectiveName());
                                                        statement4.setString(3, user.getEffectiveName());
                                                        statement4.setString(4, user.getId());
                                                        statement4.setString(5, role.getId());
                                                        statement4.setString(6, role.getName());

                                                        int rowsAffected2 = statement4.executeUpdate();

                                                        if (rowsAffected2 > 0)
                                                        {
                                                            System.out.println("Cargo dado a um novo membro e foi inserido com sucesso!");

                                                            event.getGuild().addRoleToMember(user, role).queue();
                                                            EmbedBuilder embed = new EmbedBuilder();
                                                            embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                                            embed.setTitle("Cargo Atribuído!");
                                                            embed.setThumbnail(user.getAvatarUrl());
                                                            embed.setDescription("Um novo cargo foi atribuído a um membro.");
                                                            embed.addField("Membro:", user.getAsMention(), false);
                                                            embed.addField("Cargo:", role.getAsMention(), false);
                                                            embed.setImage("https://imgur.com/hxT05ye.gif");
                                                            embed.setFooter(dataFormatada, event.getGuild().getIconUrl());

                                                            event.getHook().sendMessageEmbeds(embed.build()).queue();
                                                        }
                                                        else
                                                        {
                                                            System.out.println("Falha ao inserir dados do novo membro a quem o cargo foi atribuído");
                                                        }

                                                    }
                                                    catch (SQLException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    else if(limite == 12)
                                    {
                                        event.getHook().sendMessage("Você já atingiu o limite estabelecido de pessoas que poderiam dar o cargo!").queue();
                                    }


                                }
                                catch (SQLException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                            else
                            {
                                event.getHook().sendMessage("Para dar o cargo, você precisa criá-lo primeiro. Utilize o comando `/criar eclipsefriend`").queue();
                            }

                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if(command.equals("remove"))
        {
            if(!hasEclipseRole)
            {
                event.getHook().sendMessage("Você não possui acesso a esse comando. Será necessário obter o **VIP Eclipse**").queue();
            }
            else if(hasEclipseRole && !hasLuaCheiaRole && !hasMinguante)
            {
                String subcommand = event.getSubcommandName();

                if(subcommand.equals("eclipsefriend"))
                {
                    User user = event.getOption("nome").getAsUser();

                    if(member.getId().equals(user.getId()))
                    {
                        event.getHook().sendMessage("Você não pode remover o próprio cargo").queue();
                    }
                    else
                    {
                        //objetivos: como vou remover o cargo de alguém, preciso primeiro verificar se a pessoa que está dando esse comando já criou o seu cargo. Para isso, c consulte o createdFriendRoleID
                        try
                        {
                            String query = "select createdFriendRoleID from eclipsefriend where memberID = ?";
                            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                            statement.setString(1, member.getId());
                            ResultSet resultset = statement.executeQuery();

                            String getcreatedFriendRoleID = null;
                            if(resultset.next())
                            {
                                getcreatedFriendRoleID = resultset.getString("createdFriendRoleID");
                                try
                                {
                                    String query2 = "select removed, memberID, friendID from eclipsefriend where memberID = ? and friendID = ?";
                                    PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                    statement2.setString(1, member.getId());
                                    statement2.setString(2, user.getId());
                                    ResultSet resultset2 = statement2.executeQuery();

                                    String pegarRemoved = null;
                                    if(resultset2.next())
                                    {
                                        pegarRemoved = resultset2.getString("removed");

                                        if(pegarRemoved == null || pegarRemoved.equals("0"))
                                        {
                                            Role role = event.getGuild().getRoleById(getcreatedFriendRoleID);

                                            event.getGuild().removeRoleFromMember(user, role).queue();

                                            // ---------------------------------------------------------------------------------------
                                            // Define o formato da data e hora
                                            SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                            formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                                            // Obtém a data e hora atual e formata
                                            Date data = new Date();
                                            String dataFormatada = formatar.format(data);

                                            // ---------
                                            //como eu encontrei no banco de dados, vou remover o cargo e setar o friendName e friendID como null
                                            EmbedBuilder embed = new EmbedBuilder();
                                            embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                            embed.setTitle("Cargo Removido!");
                                            embed.setThumbnail(user.getAvatarUrl());
                                            embed.setDescription("O cargo foi removido com sucesso!");
                                            embed.addField("Membro:", user.getAsMention(), false);
                                            embed.addField("Cargo removido:", role.getAsMention(), false);
                                            embed.setImage("https://imgur.com/hxT05ye.gif");
                                            embed.setFooter(dataFormatada, event.getGuild().getIconUrl());

                                            event.getHook().sendMessageEmbeds(embed.build()).queue();

                                            try
                                            {
                                                String query3 = "update eclipsefriend set removed = true where memberID = ? and friendID = ?";
                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                statement3.setString(1, member.getId());
                                                statement3.setString(2, user.getId());

                                                int affectedRows = statement3.executeUpdate();

                                                if(affectedRows > 0)
                                                {
                                                    System.out.println("Exito ao atualizar a remoção do membro no banco de dados");
                                                }
                                                else
                                                {
                                                    System.out.println("Ocorreu uma falha ao remover o membro do banco de dados");
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        else if(pegarRemoved.equals("1"))
                                        {
                                            event.getHook().sendMessage("Você já removeu o cargo dessa pessoa").queue();
                                        }
                                    }
                                    else
                                    {
                                        event.getHook().sendMessage("Você ainda não deu o cargo para essa pessoa").queue();
                                    }


                                }
                                catch(SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                event.getHook().sendMessage("Você ainda não criou o seu cargo **EclipseFriend**! Para criá-lo, digite `/criar eclipsefriend`").queue();
                            }
                        }
                        catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

        if (command.equals("moonvip"))
        {
            if(!hasLuaCheiaRole)
            {
                event.getHook().sendMessage("Você não possui acesso a esse comando. Será necessário obter o **VIP Lua-Cheia**").queue();
            }
            else if (hasLuaCheiaRole && !hasEclipseRole & !hasMinguante)
            {
                String subcommand = event.getSubcommandName();

                if (subcommand.equals("criar"))
                {
                    // Deferindo a resposta da interação para evitar múltiplas respostas


                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                    {
                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").setEphemeral(true).queue();
                        return;
                    }

                    if (emoji.size() != 1)
                    {
                        event.getHook().sendMessage("Insira um emoji válido").setEphemeral(true).queue();
                        return;
                    }

                    final var emojiImage = emoji.get(0).getImage();
                    emojiImage.download().whenComplete((inputStream, exception) ->
                    {
                        if (exception != null)
                        {
                            // Enviando uma mensagem de erro
                            event.getHook().sendMessage("Ocorreu um erro ao baixar a imagem do emoji").setEphemeral(true).queue();
                        }
                        else
                        {
                            try
                            {
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                boolean success = convertImg(inputStream, outputStream, "png");

                                if (success)
                                {
                                    String query = "SELECT memberID from moonvip where memberID = ?";
                                    try
                                    {
                                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                                        statement.setString(1, member.getId());
                                        ResultSet rs = statement.executeQuery();

                                        if (rs.next())
                                        {
                                            event.getHook().sendMessage("Seu `moonvip` já foi criado, não pode criar de novo!").queue();
                                        }
                                        else
                                        {
                                            System.out.println("Conversão concluída com sucesso.");
                                            InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());
                                            Icon icon = Icon.from(pngInputStream);

                                            RoleAction roleAction = event.getGuild().createRole();
                                            roleAction.setName(cargo);
                                            roleAction.setColor(Color.decode(corCargo));
                                            roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                            roleAction.setIcon(icon).queue(role ->
                                            {

                                                String cargoMencionado = role.getAsMention();
                                                EmbedBuilder embedCriar = new EmbedBuilder()
                                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                        .setTitle("<a:moon:1280355998112026746> Criação de VIP Lua-Cheia <a:moon:1280355998112026746>")
                                                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                        .addField("`Cargo`", cargoMencionado, true)
                                                        .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                        .addField("────────────────────────────────", "⭐ Aproveite benefícios VIP!", false)
                                                        .setDescription("Parabéns! Seu VIP foi criado com sucesso! 🎉")
                                                        .setFooter("Sua experiência VIP está apenas começando! ✨", event.getGuild().getIconUrl())
                                                        .setImage("https://imgur.com/AHpFIQK.gif")
                                                        .setColor(role.getColor());

                                                guild.modifyRolePositions().selectPosition(role).moveBelow(event.getGuild().getRoleById("1224171011721924722")).queue();
                                                guild.addRoleToMember(member, role).queue();

                                                // Enviando a embed de sucesso
                                                event.getHook().sendMessageEmbeds(embedCriar.build()).queue();

                                                // Enviando a embed diretamente para o canal, sem ser efêmera
                                                /*
                                                TextChannel channel = event.getChannel().asTextChannel();
                                                channel.sendMessageEmbeds(embedCriar.build()).queue(); // Embed pública para o canal*/

                                                try
                                                {
                                                    String query_2 = "INSERT INTO moonvip (nome, memberID, idVip, tipo, idCustomRole, nameCustomRole) VALUES (?, ?, ?, ?, ?, ?)";
                                                    PreparedStatement statement_2 = ConnectionDB.getConexao().prepareStatement(query_2);
                                                    statement_2.setString(1, member.getEffectiveName());
                                                    statement_2.setString(2, member.getId());
                                                    statement_2.setString(3, luacheia.getId().toString());
                                                    statement_2.setString(4, "Lua-Cheia");
                                                    statement_2.setString(5, role.getId());
                                                    statement_2.setString(6, role.getName());

                                                    int rowsAffected = statement_2.executeUpdate();
                                                    if (rowsAffected > 0)
                                                    {
                                                        System.out.println("Novo membro moonvip inserido no banco de dados");
                                                    }
                                                    else
                                                    {
                                                        System.out.println("Ocorreu um erro ao inserir um comprador moonvip");
                                                    }
                                                }
                                                catch (SQLException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }
                                    }
                                    catch (SQLException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    System.out.println("Falha na conversão da imagem do " + member.getEffectiveName());
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                System.out.println("Erro ao processar a imagem do emoji do membro " + member.getEffectiveName());
                            }
                        }
                    });
                }
                else if(subcommand.equals("editar"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    String query = "select * from moonvip where memberID = ?";
                    try
                    {
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        if(resultset.next())
                        {
                            try
                            {
                                String query2 = "SELECT idCustomRole FROM moonvip WHERE memberID = ?";

                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                                statement2.setString(1, member.getId());

                                ResultSet resultset2 = statement2.executeQuery();

                                String getquery = null;

                                if(resultset2.next())
                                {
                                    getquery = resultset2.getString("idCustomRole");
                                }

                                if(getquery != null)
                                {
                                    Role role = event.getGuild().getRoleById(getquery);
                                    // Trate o caso em que loverIdRole não foi encontrado para os IDs fornecidos
                                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                    {
                                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                                        return;
                                    }

                                    if (emoji.size() != 1)
                                    {
                                        event.getHook().sendMessage("Insira um emoji válido").queue();
                                        return;
                                    }

                                    final var emojiImage = emoji.get(0).getImage();
                                    emojiImage.download().whenComplete((inputStream, exception) ->
                                    {
                                        if (exception != null)
                                        {
                                            //reply error
                                            System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                        }
                                        else
                                        {
                                            //Create the icon from the input stream
                                            try
                                            {
                                                // Criar um OutputStream para armazenar a imagem convertida
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                                // Converter a imagem de GIF para PNG
                                                boolean success = convertImg(inputStream, outputStream, "png");

                                                if (success)
                                                {
                                                    System.out.println("Conversão concluída com sucesso.");
                                                    // Aqui você pode usar o OutputStream como necessário
                                                    // Converter o OutputStream para InputStream para criar o Icon
                                                    InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                                    // Criar o Icon a partir do InputStream
                                                    Icon icon = Icon.from(pngInputStream);

                                                    // Use o Icon para definir o ícone do cargo no Discord
                                                    role.getManager()
                                                            .setName(cargo)
                                                            .setColor(Color.decode(corCargo))
                                                            .setPermissions(Collections.singletonList(Permission.MESSAGE_SEND))
                                                            .setIcon(icon).queue();
                                                }
                                                else
                                                {
                                                    System.out.println("Falha na conversão.");
                                                }

                                                // Cargo criado com sucesso
                                                String cargoMencionado = role.getAsMention();
                                                EmbedBuilder embedCriar = new EmbedBuilder()
                                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                        .setTitle("<a:moon:1280355998112026746> Edição de VIP <a:moon:1280355998112026746>")
                                                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                        .addField("`Novo cargo`", cargoMencionado, true)
                                                        .addField("`Novo emoji`", emoji.get(0).getAsMention(), true)
                                                        .addField("────────────────────────────────", "⭐ Aproveite seus novos benefícios VIP!", false)
                                                        .setDescription("Seu cargo foi editado com sucesso! 🎉")
                                                        .setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl())
                                                        .setImage("https://imgur.com/t9UX08j.gif")
                                                        .setColor(role.getColor());

                                                event.getHook().sendMessageEmbeds(embedCriar.build()).queue();
                                                //tem que botar .queue aqui no debaixo, se não, não acontece nada

                                                try
                                                {
                                                    String query3 = "update moonvip set nameCustomRole = ? where idCustomRole = ?";
                                                    PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                    statement3.setString(1, cargo);
                                                    statement3.setString(2, role.getId());

                                                    int rowsAffected = statement3.executeUpdate();

                                                    if(rowsAffected > 0)
                                                    {
                                                        System.out.println("Cargo atualizado com sucesso");
                                                    }
                                                    else
                                                    {
                                                        System.out.println("Não teve sucesso ao atualizar os cargos");
                                                    }
                                                }
                                                catch (SQLException e)
                                                {
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch(IOException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                                else
                                {
                                    event.getHook().sendMessage("Para editar seu cargo, você deve criar o próprio cargo em `/criar eclipsevip`!").queue();
                                }


                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }

                        }

                        else
                        {
                            event.getHook().sendMessage("Seu **VIP** ainda não está registrado. Use o comando `/criar eclipsevip` ou `/criar eclipselover` para se registrar!").queue();;
                        }
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }

                else if(subcommand.equals("call"))
                {
                    String query = "select idCustomRole, callID from moonvip where memberID = ?";
                    PreparedStatement statement = null;

                    try
                    {
                        statement = ConnectionDB.getConexao().prepareStatement(query);
                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        String getidCustomRole = null;
                        String getcallID = null;
                        if(resultset.next())
                        {
                            getidCustomRole = resultset.getString("idCustomRole");
                            getcallID = resultset.getString("callID");
                            if(getidCustomRole != null && getcallID != null)
                            {
                                event.getHook().sendMessage("Você já criou sua call, não será possível criar outra!").queue();
                            }
                            else if(getidCustomRole != null && getcallID == null)
                            {
                                try
                                {
                                    String query3 = "select createdFriendRoleID from moonfriend where memberID = ?";
                                    PreparedStatement  statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                    statement3.setString(1, member.getId());
                                    ResultSet resultset3 = statement3.executeQuery();

                                    if(resultset3.next())
                                    {
                                        String callEclipse = event.getOption("nome").getAsString();
                                        String getcreatedFriendRoleID = resultset3.getString("createdFriendRoleID");

                                        event.getGuild().createVoiceChannel("Canal de voz")
                                                .setName(callEclipse)
                                                .setParent(luaCheiaCattegory)
                                                .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_START_ACTIVITIES, Permission.VOICE_STREAM, Permission.VOICE_SET_STATUS), Collections.emptyList())  // Allow specific permissions to member
                                                .addPermissionOverride(event.getGuild().getRoleById(getcreatedFriendRoleID), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_START_ACTIVITIES, Permission.VOICE_STREAM), Collections.emptyList())  // Allow specific permissions to staff role
                                                .addPermissionOverride(guild.getPublicRole(), EnumSet.of(Permission.VIEW_CHANNEL), EnumSet.of(Permission.VOICE_CONNECT))  // Nega permissões para o everyone
                                                .queue(channel ->
                                                        {
                                                            event.getHook().sendMessage("O canal de voz " + "`" + channel.getName() + "`" + " foi criado em <#" + channel.getId() + ">").queue();

                                                            try
                                                            {
                                                                String query2 = "update moonvip set callName = ?, callID = ? where memberID = ?";
                                                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                                                statement2.setString(1, channel.getName());
                                                                statement2.setString(2, channel.getId());
                                                                statement2.setString(3, member.getId());

                                                                int rowsAffected = statement2.executeUpdate();

                                                                if(rowsAffected > 0)
                                                                {
                                                                    System.out.println("Dados da call inseridas com sucesso!");
                                                                }
                                                                else
                                                                {
                                                                    System.out.println("Ocorreu um erro ao inserir os dados da call!");
                                                                }
                                                            }
                                                            catch(SQLException e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        },
                                                        error ->
                                                        {
                                                            event.getHook().sendMessage("Ocorreu um erro ao criar o canal de voz: " + error.getMessage()).queue();
                                                        });
                                    }
                                    else
                                    {
                                        event.getHook().sendMessage("Para criar a call, crie o cargo **Moon Friend** com o comando `/moonfriend criar`").queue();
                                    }
                                }
                                catch(SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            event.getHook().sendMessage("Você ainda não utilizou um comando do VIP. Digite `/moonvip criar` e `/moonfriend criar` para utilizar este comando!").queue();
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(command.equals("moonfriend"))
        {
            if(!hasLuaCheiaRole)
            {
                event.getHook().sendMessage("Você não possui acesso a esse comando. Será necessário obter o **VIP Lua-Cheia**").queue();
            }
            else if(hasLuaCheiaRole && !hasEclipseRole)
            {
                String subcommand = event.getSubcommandName();

                if(subcommand.equals("criar"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    //verificar se usou moonvip criar
                    try
                    {
                        String query = "select idCustomRole from moonvip where memberID = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        String getidCustomRole = null;
                        if(resultset.next())
                        {
                            //agora, eu preciso ver se eu já criei o cargo. Se não criei, crio. Se criei, falo que não pode mais criar
                            getidCustomRole = resultset.getString("idCustomRole");

                            try
                            {
                                Role idcustomrole = event.getGuild().getRoleById(getidCustomRole);

                                //eu só posso criar o cargo se eu já tiver usado o comando, mas a pessoa com a tag pode dar esse comando
                                String query2 = "select createdFriendRoleID from moonfriend where memberID = ?";
                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                statement2.setString(1, member.getId());
                                ResultSet resultset2 = statement2.executeQuery();
                                if(resultset2.next() == true)
                                {
                                    event.getHook().sendMessage("Você já criou o cargo, não pode mais criar outro. Caso queira editar o cargo, digite `/moonfriend editar`").setEphemeral(true).queue();
                                }
                                else
                                {
                                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                    {
                                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").setEphemeral(true).queue();
                                        return;
                                    }

                                    if (emoji.size() != 1)
                                    {
                                        event.getHook().sendMessage("Insira um emoji válido").setEphemeral(true).queue();
                                        return;
                                    }

                                    final var emojiImage = emoji.get(0).getImage();
                                    emojiImage.download().whenComplete((inputStream, exception) ->
                                    {
                                        if (exception != null)
                                        {
                                            //reply error
                                            System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                        }
                                        else
                                        {
                                            //Create the icon from the input stream
                                            try
                                            {
                                                // Criar um OutputStream para armazenar a imagem convertida
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                                // Converter a imagem de GIF para PNG
                                                boolean success = convertImg(inputStream, outputStream, "png");

                                                if (success)
                                                {
                                                    System.out.println("Conversão concluída com sucesso.");
                                                    // Aqui você pode usar o OutputStream como necessário
                                                    // Converter o OutputStream para InputStream para criar o Icon
                                                    InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                                    // Criar o Icon a partir do InputStream
                                                    Icon icon = Icon.from(pngInputStream);
                                                    //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                                    RoleAction roleAction = event.getGuild().createRole();
                                                    roleAction.setName(cargo);
                                                    roleAction.setColor(Color.decode(corCargo));
                                                    roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                                    roleAction.setIcon(icon).queue(role ->
                                                    {

                                                        // Cargo criado com sucesso
                                                        String cargoMencionado = role.getAsMention();
                                                        EmbedBuilder embedCriar = new EmbedBuilder()
                                                                .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                                .setTitle("<a:moon:1280355998112026746> Moon Friend <a:moon:1280355998112026746>")
                                                                .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                                .addField("`Cargo Compartilhado`", cargoMencionado, true)
                                                                .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                                .addField("────────────────────────────────", "⭐ Aproveite benefícios VIP!", false)
                                                                .setDescription("Cargo Moon Friend criado com sucesso, aproveite!")
                                                                .setFooter("Aproveite o seu cargo!", event.getGuild().getIconUrl())
                                                                .setImage("https://imgur.com/8vlquSJ.gif")
                                                                .setColor(role.getColor());

                                                        //tem que botar .queue aqui no debaixo, se não, não acontece nada
                                                        guild.modifyRolePositions().selectPosition(role).moveAbove(event.getGuild().getRoleById("1223394386558320680")).queue();
                                                        guild.addRoleToMember(member, role).queue();

                                                        event.getHook().sendMessageEmbeds(embedCriar.build()).queue();


                                                        String query3 = "INSERT INTO moonfriend(memberID, nome, createdFriendRoleID, createdFriendRoleName) VALUES (?, ?, ?, ?)";

                                                        try
                                                        {
                                                            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                            statement3.setString(1, member.getId());
                                                            statement3.setString(2, member.getEffectiveName());
                                                            statement3.setString(3, role.getId());
                                                            statement3.setString(4, role.getName());

                                                            int rowsAffected2 = statement3.executeUpdate();

                                                            if (rowsAffected2 > 0)
                                                            {
                                                                System.out.println("Dados inseridos com sucesso!");
                                                            }
                                                            else
                                                            {
                                                                System.out.println("Falha ao inserir dados.");
                                                            }

                                                        }
                                                        catch (SQLException e)
                                                        {
                                                            // TODO: handle exception
                                                        }

                                                    });
                                                }
                                                else
                                                {
                                                    System.out.println("Falha na conversão.");
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                // TODO: handle exception
                                            }

                                        }
                                    });
                                }

                            }
                            catch(SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            event.getHook().sendMessage("Você ainda não utilizou nenhum comando do VIP. Utilize `/moonvip criar`").queue();
                        }
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(subcommand.equals("editar"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    try
                    {
                        //para dar o cargo para alguém, tenho que ter criado o cargo primeiro, então preciso verificar no banco de dados
                        String query = "select friendID, createdFriendRoleID from moonfriend where memberID = ?";
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        String getFriendRoleID = null;
                        String getfriendID = null;
                        //retorna true se o membro já estiver registrado no banco de dados, que significa que já criei o cargo
                        if(resultset.next())
                        {
                            getFriendRoleID = resultset.getString("createdFriendRoleID");
                            getfriendID = resultset.getString("friendID");

                            Role role = event.getGuild().getRoleById(getFriendRoleID);
                            Member member2 = event.getMember();

                            // ---------------------------------------------------------------------------------------
                            // Define o formato da data e hora
                            SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                            formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                            // Obtém a data e hora atual e formata
                            Date data = new Date();
                            String dataFormatada = formatar.format(data);

                            // ---------------------------------------------------------------------------------------
                            if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                            {
                                event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                                return;
                            }

                            if (emoji.size() != 1)
                            {
                                event.getHook().sendMessage("Insira um emoji válido").queue();
                                return;
                            }

                            final var emojiImage = emoji.get(0).getImage();
                            emojiImage.download().whenComplete((inputStream, exception) ->
                            {
                                if (exception != null)
                                {
                                    //reply error
                                    System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                }
                                else
                                {
                                    //Create the icon from the input stream
                                    try
                                    {
                                        // Criar um OutputStream para armazenar a imagem convertida
                                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                        // Converter a imagem de GIF para PNG
                                        boolean success = convertImg(inputStream, outputStream, "png");

                                        if (success)
                                        {
                                            System.out.println("Conversão concluída com sucesso.");
                                            // Aqui você pode usar o OutputStream como necessário
                                            // Converter o OutputStream para InputStream para criar o Icon
                                            InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                            // Criar o Icon a partir do InputStream
                                            Icon icon = Icon.from(pngInputStream);
                                            //String emojiID = "\uD83D\uDE0A"; // Exemplo de emoji: 😊
                                            role.getManager()
                                                    .setName(cargo)
                                                    .setColor(Color.decode(corCargo))
                                                    .setPermissions(Collections.singletonList(Permission.MESSAGE_SEND))
                                                    .setIcon(icon).queue();

                                            // Cargo criado com sucesso
                                            String cargoMencionado = role.getAsMention();
                                            EmbedBuilder embedCriar = new EmbedBuilder()
                                                    .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                    .setTitle("<a:6954sylveonhappy:1228896788921192458> Edição de VIP <a:6954sylveonhappy:1228896788921192458>")
                                                    .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                    .addField("`Novo cargo`", cargoMencionado, true)
                                                    .addField("`Novo emoji`", emoji.get(0).getAsMention(), true)
                                                    .addField("────────────────────────────────", "⭐ Aproveite seus benefícios VIP!", false)
                                                    .setDescription("Seu cargo foi editado com sucesso! 🎉")
                                                    .setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl())
                                                    .setImage("https://imgur.com/MyjWslJ.gif")
                                                    .setColor(role.getColor());

                                            event.getHook().sendMessageEmbeds(embedCriar.build()).queue();
                                            //tem que botar .queue aqui no debaixo, se não, não acontece nada

                                            try
                                            {
                                                String query3 = "update moonfriend set createdFriendRoleName = ? where memberID = ? or friendID = ?";
                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                statement3.setString(1, cargo);
                                                statement3.setString(2, member.getId());
                                                statement3.setString(3, member2.getId());

                                                int rowsAffected = statement3.executeUpdate();

                                                if (rowsAffected > 0)
                                                {
                                                    System.out.println("Cargo atualizado com sucesso");
                                                }
                                                else
                                                {
                                                    System.out.println("Não teve sucesso ao atualizar os cargos");
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("Falha na conversão.");
                                        }
                                    }
                                    catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                        else
                        {
                            event.getHook().sendMessage("Para editar o cargo, você precisa criá-lo primeiro. Utilize o comando `/moonfriend criar`").queue();
                        }

                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }

                else if(subcommand.equals("give"))
                {
                    User user = event.getOption("nome").getAsUser();

                    if(member.getId().equals(user.getId()))
                    {
                        event.getHook().sendMessage("Você já possui o cargo, só pode dar ele para outro membro do servidor").queue();
                    }
                    else
                    {
                        try
                        {
                            //para dar o cargo para alguém, tenho que ter criado o cargo primeiro, então preciso verificar no banco de dados
                            String query = "select friendID, createdFriendRoleID, removed from moonfriend where memberID = ?";
                            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                            statement.setString(1, member.getId());

                            ResultSet resultset = statement.executeQuery();

                            String getFriendRoleID = null;
                            String getfriendID = null;
                            String cargoremovido = null;
                            //retorna true se o membro já estiver registrado no banco de dados, que significa que já criei o cargo
                            if(resultset.next())
                            {
                                try
                                {
                                    String querychan = "SELECT COUNT(*) AS count FROM moonfriend WHERE memberID = ? AND (removed IS NULL OR removed = '0')";

                                    PreparedStatement statement6 = ConnectionDB.getConexao().prepareStatement(querychan);

                                    statement6.setString(1, member.getId());

                                    ResultSet resultset6 = statement6.executeQuery();

                                    int limite = 0;
                                    if(resultset6.next())
                                    {
                                        limite = resultset6.getInt("count");
                                    }

                                    cargoremovido = resultset.getString("removed");
                                    if(limite >= 0 && limite != 8)
                                    {
                                        System.out.println(member.getEffectiveName() + "Deu o cargo para " + limite + " pessoas!");
                                        getfriendID = resultset.getString("friendID");
                                        getFriendRoleID = resultset.getString("createdFriendRoleID");

                                        Role role = event.getGuild().getRoleById(getFriendRoleID);

                                        // ---------------------------------------------------------------------------------------
                                        // Define o formato da data e hora
                                        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                        formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                                        // Obtém a data e hora atual e formata
                                        Date data = new Date();
                                        String dataFormatada = formatar.format(data);

                                        // ---------------------------------------------------------------------------------------

                                        if(getfriendID == null) //significa que não deu o cargo pra ninguém, entao faço um update
                                        {
                                            try
                                            {
                                                String query2 = "update moonfriend set friendName = ?, friendID = ? where memberID = ?";
                                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                                statement2.setString(1, user.getEffectiveName());
                                                statement2.setString(2, user.getId());
                                                statement2.setString(3, member.getId());

                                                int rowsAffected = statement2.executeUpdate();

                                                if(rowsAffected > 0)
                                                {
                                                    System.out.println("eclipsefriend inserido com sucesso!");

                                                    event.getGuild().addRoleToMember(user, role).queue();
                                                    EmbedBuilder embed = new EmbedBuilder();
                                                    embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                                    embed.setTitle("Cargo Atribuído!");
                                                    embed.setThumbnail(user.getAvatarUrl());
                                                    embed.setDescription("Um novo cargo foi atribuído a um membro.");
                                                    embed.addField("Membro:", user.getAsMention(), false);
                                                    embed.addField("Cargo:", role.getAsMention(), false);
                                                    embed.addField("────────────────────────────────", "⭐ Aproveite seus novos benefícios VIP!", false);
                                                    embed.setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl());
                                                    embed.setImage("https://imgur.com/14uWdf8.gif");

                                                    event.getHook().sendMessageEmbeds(embed.build()).queue();
                                                }
                                                else
                                                {
                                                    System.out.printf("Ocorreu um erro ao inserir dados no elcipsefriend!");
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }

                                        else if(getfriendID != null) //signfica que já dei cargo para alguem
                                        {
                                            //primeiro, vamos ver se eu não estou inserindo a mesma pessoa duas vezes
                                            try
                                            {
                                                String query3 = "select removed, friendID from moonfriend where memberID = ? and friendID = ?";
                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                statement3.setString(1, member.getId());
                                                statement3.setString(2, user.getId());
                                                ResultSet resultset3 = statement3.executeQuery();

                                                String getremoved = resultset3.getString("removed");
                                                if (resultset3.next()) //estou tentando dar o cargo ao mesmo membro
                                                {
                                                    if (getremoved == null || getremoved.equals("0"))
                                                    {
                                                        event.getHook().sendMessage("Você não pode dar o cargo pra mesma pessoa duas vezes!").queue();
                                                    }

                                                    else if(getremoved.equals("1"))
                                                    {
                                                        try
                                                        {
                                                            System.out.println("Cargo dado a um novo membro e foi inserido com sucesso!");

                                                            event.getGuild().addRoleToMember(user, role).queue();

                                                            EmbedBuilder embed = new EmbedBuilder();
                                                            embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                                            embed.setTitle("Cargo Atribuído!");
                                                            embed.setThumbnail(user.getAvatarUrl());
                                                            embed.setDescription("Um novo cargo foi atribuído a um membro.");
                                                            embed.addField("Membro:", user.getAsMention(), false);
                                                            embed.addField("Cargo:", role.getAsMention(), false);
                                                            embed.addField("────────────────────────────────", "⭐ Aproveite seus novos benefícios VIP!", false);
                                                            embed.setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl());
                                                            embed.setImage("https://imgur.com/14uWdf8.gif");

                                                            event.getHook().sendMessageEmbeds(embed.build()).queue();

                                                            String query4 = "update moonfriend set removed = false where memberID = ? and friendID = ?";
                                                            PreparedStatement statement4 = ConnectionDB.getConexao().prepareStatement(query4);

                                                            statement4.setString(1, member.getId());
                                                            statement4.setString(2, user.getId());

                                                            int rowsAffected = statement4.executeUpdate();

                                                            if(rowsAffected > 0)
                                                            {
                                                                System.out.println("Exito ao atualizar o removed");

                                                                event.reply("Você não pode dar o cargo pra mesma pessoa duas vezes!").queue();
                                                            }
                                                            else
                                                            {
                                                                System.out.println("Não deu certo de atualizar o removed");
                                                            }
                                                        }
                                                        catch(SQLException e)
                                                        {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }
                                                else
                                                {
                                                    String query4 = "INSERT INTO moonfriend(memberID, nome, friendName, friendID, createdFriendRoleID, createdFriendRoleName) VALUES (?, ?, ?, ?, ?, ?)";

                                                    try
                                                    {
                                                        PreparedStatement statement4 = ConnectionDB.getConexao().prepareStatement(query4);

                                                        statement4.setString(1, member.getId());
                                                        statement4.setString(2, member.getEffectiveName());
                                                        statement4.setString(3, user.getEffectiveName());
                                                        statement4.setString(4, user.getId());
                                                        statement4.setString(5, role.getId());
                                                        statement4.setString(6, role.getName());

                                                        int rowsAffected2 = statement4.executeUpdate();

                                                        if (rowsAffected2 > 0)
                                                        {
                                                            System.out.println("Cargo dado a um novo membro e foi inserido com sucesso!");

                                                            event.getGuild().addRoleToMember(user, role).queue();
                                                            EmbedBuilder embed = new EmbedBuilder();
                                                            embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                                            embed.setTitle("Cargo Atribuído!");
                                                            embed.setThumbnail(user.getAvatarUrl());
                                                            embed.setDescription("Um novo cargo foi atribuído a um membro.");
                                                            embed.addField("Membro:", user.getAsMention(), false);
                                                            embed.addField("Cargo:", role.getAsMention(), false);
                                                            embed.addField("────────────────────────────────", "⭐ Aproveite seus benefícios VIP!", false);
                                                            embed.setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl());
                                                            embed.setImage("https://imgur.com/hxT05ye.gif");

                                                            event.getHook().sendMessageEmbeds(embed.build()).queue();
                                                        }
                                                        else
                                                        {
                                                            System.out.println("Falha ao inserir dados do novo membro a quem o cargo foi atribuído");
                                                        }

                                                    }
                                                    catch (SQLException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    else if(limite == 8)
                                    {
                                        event.getHook().sendMessage("Você já atingiu o limite estabelecido de pessoas que poderiam dar o cargo!").queue();
                                    }


                                }
                                catch (SQLException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                            else
                            {
                                event.getHook().sendMessage("Para dar o cargo, você precisa criá-lo primeiro. Utilize o comando `/moonfriend criar`").queue();
                            }

                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else if(subcommand.equals("remove"))
                {
                    User user = event.getOption("nome").getAsUser();

                    if(member.getId().equals(user.getId()))
                    {
                        event.getHook().sendMessage("Você não pode remover o próprio cargo").queue();
                    }
                    else
                    {
                        //objetivos: como vou remover o cargo de alguém, preciso primeiro verificar se a pessoa que está dando esse comando já criou o seu cargo. Para isso, c consulte o createdFriendRoleID
                        try
                        {
                            String query = "select createdFriendRoleID from moonfriend where memberID = ?";
                            PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                            statement.setString(1, member.getId());
                            ResultSet resultset = statement.executeQuery();

                            String getcreatedFriendRoleID = null;
                            if(resultset.next())
                            {
                                getcreatedFriendRoleID = resultset.getString("createdFriendRoleID");
                                try
                                {
                                    String query2 = "select removed, memberID, friendID from moonfriend where memberID = ? and friendID = ?";
                                    PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);

                                    statement2.setString(1, member.getId());
                                    statement2.setString(2, user.getId());
                                    ResultSet resultset2 = statement2.executeQuery();

                                    String pegarRemoved = null;
                                    if(resultset2.next())
                                    {
                                        pegarRemoved = resultset2.getString("removed");

                                        if(pegarRemoved == null || pegarRemoved.equals("0"))
                                        {
                                            Role role = event.getGuild().getRoleById(getcreatedFriendRoleID);

                                            event.getGuild().removeRoleFromMember(user, role).queue();

                                            // ---------------------------------------------------------------------------------------
                                            // Define o formato da data e hora
                                            SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                            formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                                            // Obtém a data e hora atual e formata
                                            Date data = new Date();
                                            String dataFormatada = formatar.format(data);

                                            // ---------
                                            //como eu encontrei no banco de dados, vou remover o cargo e setar o friendName e friendID como null
                                            EmbedBuilder embed = new EmbedBuilder();
                                            embed.setColor(role.getColor()); // Define a cor do embed como a cor do cargo
                                            embed.setTitle("Cargo Removido!");
                                            embed.setThumbnail(user.getAvatarUrl());
                                            embed.setDescription("O cargo foi removido com sucesso!");
                                            embed.addField("Membro:", user.getAsMention(), false);
                                            embed.addField("Cargo removido:", role.getAsMention(), false);
                                            embed.addField("────────────────────────────────", "⭐ Aproveite seus benefícios VIP!", false);
                                            embed.setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl());
                                            embed.setImage("https://imgur.com/Arczvz2.gif");
                                            embed.setFooter(dataFormatada, event.getGuild().getIconUrl());

                                            event.getHook().sendMessageEmbeds(embed.build()).queue();

                                            try
                                            {
                                                String query3 = "update moonfriend set removed = true where memberID = ? and friendID = ?";
                                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                statement3.setString(1, member.getId());
                                                statement3.setString(2, user.getId());

                                                int affectedRows = statement3.executeUpdate();

                                                if(affectedRows > 0)
                                                {
                                                    System.out.println("Exito ao atualizar a remoção do membro no banco de dados");
                                                }
                                                else
                                                {
                                                    System.out.println("Ocorreu uma falha ao remover o membro do banco de dados");
                                                }
                                            }
                                            catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        else if(pegarRemoved.equals("1"))
                                        {
                                            event.getHook().sendMessage("Você já removeu o cargo dessa pessoa").queue();
                                        }
                                    }
                                    else
                                    {
                                        event.getHook().sendMessage("Você ainda não deu o cargo para essa pessoa").queue();
                                    }


                                }
                                catch(SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                event.getHook().sendMessage("Você ainda não criou o seu cargo **Moon Friend**! Para criá-lo, digite `/moonfriend criar`").queue();
                            }
                        }
                        catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        else if(command.equals("minguantevip"))
        {
            if(!hasMinguante)
            {
                event.getHook().sendMessage("Você não tem acesso a este comando. Será necessário obter o **VIP Minguante**").queue();
            }
            else if(hasMinguante && !hasEclipseRole && !hasLuaCheiaRole)
            {
                String subcommand = event.getSubcommandName();

                if (subcommand.equals("criar"))
                {
                    // Deferindo a resposta da interação para evitar múltiplas respostas


                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                    {
                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").setEphemeral(true).queue();
                        return;
                    }

                    if (emoji.size() != 1)
                    {
                        event.getHook().sendMessage("Insira um emoji válido").setEphemeral(true).queue();
                        return;
                    }

                    final var emojiImage = emoji.get(0).getImage();
                    emojiImage.download().whenComplete((inputStream, exception) ->
                    {
                        if (exception != null)
                        {
                            // Enviando uma mensagem de erro
                            event.getHook().sendMessage("Ocorreu um erro ao baixar a imagem do emoji").setEphemeral(true).queue();
                        }
                        else
                        {
                            try
                            {
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                boolean success = convertImg(inputStream, outputStream, "png");

                                if (success)
                                {
                                    String query = "SELECT memberID from minguantevip where memberID = ?";
                                    try
                                    {
                                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                                        statement.setString(1, member.getId());
                                        ResultSet rs = statement.executeQuery();

                                        if (rs.next())
                                        {
                                            event.getHook().sendMessage("Seu `minguantevip` já foi criado, não pode criar de novo!").queue();
                                        }
                                        else
                                        {
                                            System.out.println("Conversão concluída com sucesso.");
                                            InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());
                                            Icon icon = Icon.from(pngInputStream);

                                            RoleAction roleAction = event.getGuild().createRole();
                                            roleAction.setName(cargo);
                                            roleAction.setColor(Color.decode(corCargo));
                                            roleAction.setPermissions(Collections.singletonList(Permission.MESSAGE_SEND));
                                            roleAction.setIcon(icon).queue(role ->
                                            {

                                                String cargoMencionado = role.getAsMention();
                                                EmbedBuilder embedCriar = new EmbedBuilder()
                                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                        .setTitle("<a:2090moon:1280881936927297719> Criação de VIP Minguante <a:2090moon:1280881936927297719>")
                                                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                        .addField("`Cargo`", cargoMencionado, true)
                                                        .addField("`Emoji`", emoji.get(0).getAsMention(), true)
                                                        .addField("────────────────────────────────", "⭐ Aproveite seus benefícios VIP!", false)
                                                        .setDescription("Parabéns! Seu VIP foi criado com sucesso! 🎉")
                                                        .setFooter("Sua experiência VIP está apenas começando! ✨", event.getGuild().getIconUrl())
                                                        .setImage("https://imgur.com/jRcxg3f.gif")
                                                        .setColor(role.getColor());

                                                guild.modifyRolePositions().selectPosition(role).moveBelow(event.getGuild().getRoleById("1224171011721924722")).queue();
                                                guild.addRoleToMember(member, role).queue();

                                                // Enviando a embed de sucesso
                                                event.getHook().sendMessageEmbeds(embedCriar.build()).queue();

                                                // Enviando a embed diretamente para o canal, sem ser efêmera
                                                /*
                                                TextChannel channel = event.getChannel().asTextChannel();
                                                channel.sendMessageEmbeds(embedCriar.build()).queue(); // Embed pública para o canal*/

                                                try
                                                {
                                                    String query_2 = "INSERT INTO minguantevip (nome, memberID, idVip, tipo, idCustomRole, nameCustomRole) VALUES (?, ?, ?, ?, ?, ?)";
                                                    PreparedStatement statement_2 = ConnectionDB.getConexao().prepareStatement(query_2);
                                                    statement_2.setString(1, member.getEffectiveName());
                                                    statement_2.setString(2, member.getId());
                                                    statement_2.setString(3, luacheia.getId().toString());
                                                    statement_2.setString(4, "Minguante");
                                                    statement_2.setString(5, role.getId());
                                                    statement_2.setString(6, role.getName());

                                                    int rowsAffected = statement_2.executeUpdate();
                                                    if (rowsAffected > 0)
                                                    {
                                                        System.out.println("Novo membro minguantevip inserido no banco de dados");
                                                    }
                                                    else
                                                    {
                                                        System.out.println("Ocorreu um erro ao inserir um comprador minguantevip");
                                                    }
                                                }
                                                catch (SQLException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }
                                    }
                                    catch (SQLException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    System.out.println("Falha na conversão da imagem do " + member.getEffectiveName());
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                System.out.println("Erro ao processar a imagem do emoji do membro " + member.getEffectiveName());
                            }
                        }
                    });
                }
                else if(subcommand.equals("editar"))
                {
                    String cargo = event.getOption("cargo").getAsString();
                    List<CustomEmoji> emoji = event.getOption("emoji").getMentions().getCustomEmojis();
                    String corCargo = event.getOption("cor").getAsString();

                    String query = "select * from minguantevip where memberID = ?";
                    try
                    {
                        PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);
                        statement.setString(1, member.getId());

                        ResultSet resultset = statement.executeQuery();

                        if(resultset.next())
                        {
                            try
                            {
                                String query2 = "SELECT idCustomRole FROM minguantevip WHERE memberID = ?";

                                PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query2);
                                statement2.setString(1, member.getId());

                                ResultSet resultset2 = statement2.executeQuery();

                                String getquery = null;

                                if(resultset2.next())
                                {
                                    getquery = resultset2.getString("idCustomRole");
                                }

                                if(getquery != null)
                                {
                                    Role role = event.getGuild().getRoleById(getquery);
                                    // Trate o caso em que loverIdRole não foi encontrado para os IDs fornecidos
                                    if (!corCargo.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
                                    {
                                        event.getHook().sendMessage("A cor inserida não é um valor hexadecimal válido!").queue();
                                        return;
                                    }

                                    if (emoji.size() != 1)
                                    {
                                        event.getHook().sendMessage("Insira um emoji válido").queue();
                                        return;
                                    }

                                    final var emojiImage = emoji.get(0).getImage();
                                    emojiImage.download().whenComplete((inputStream, exception) ->
                                    {
                                        if (exception != null)
                                        {
                                            //reply error
                                            System.out.println("Ocorreu um erro ao baixar a imagem do emoji do membro " + member.getEffectiveName());
                                        }
                                        else
                                        {
                                            //Create the icon from the input stream
                                            try
                                            {
                                                // Criar um OutputStream para armazenar a imagem convertida
                                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                                // Converter a imagem de GIF para PNG
                                                boolean success = convertImg(inputStream, outputStream, "png");

                                                if (success)
                                                {
                                                    System.out.println("Conversão concluída com sucesso.");
                                                    // Aqui você pode usar o OutputStream como necessário
                                                    // Converter o OutputStream para InputStream para criar o Icon
                                                    InputStream pngInputStream = new ByteArrayInputStream(outputStream.toByteArray());

                                                    // Criar o Icon a partir do InputStream
                                                    Icon icon = Icon.from(pngInputStream);

                                                    // Use o Icon para definir o ícone do cargo no Discord
                                                    role.getManager()
                                                            .setName(cargo)
                                                            .setColor(Color.decode(corCargo))
                                                            .setPermissions(Collections.singletonList(Permission.MESSAGE_SEND))
                                                            .setIcon(icon).queue();
                                                }
                                                else
                                                {
                                                    System.out.println("Falha na conversão.");
                                                }

                                                // Cargo criado com sucesso
                                                String cargoMencionado = role.getAsMention();
                                                EmbedBuilder embedCriar = new EmbedBuilder()
                                                        .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                                                        .setTitle("<a:2090moon:1280881936927297719> Edição de VIP <a:2090moon:1280881936927297719>")
                                                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                                                        .addField("`Novo cargo`", cargoMencionado, true)
                                                        .addField("`Novo emoji`", emoji.get(0).getAsMention(), true)
                                                        .addField("────────────────────────────────", "⭐ Aproveite seus benefícios VIP!", false)
                                                        .setDescription("Seu cargo foi editado com sucesso! 🎉")
                                                        .setFooter("VIP • Exclusividade no Santuário", event.getGuild().getIconUrl())
                                                        .setImage("https://imgur.com/8ZJyzEJ.gif")
                                                        .setColor(role.getColor());

                                                event.getHook().sendMessageEmbeds(embedCriar.build()).queue();
                                                //tem que botar .queue aqui no debaixo, se não, não acontece nada

                                                try
                                                {
                                                    String query3 = "update minguantevip set nameCustomRole = ? where idCustomRole = ?";
                                                    PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query3);

                                                    statement3.setString(1, cargo);
                                                    statement3.setString(2, role.getId());

                                                    int rowsAffected = statement3.executeUpdate();

                                                    if(rowsAffected > 0)
                                                    {
                                                        System.out.println("Cargo atualizado com sucesso");
                                                    }
                                                    else
                                                    {
                                                        System.out.println("Não teve sucesso ao atualizar os cargos");
                                                    }
                                                }
                                                catch (SQLException e)
                                                {
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch(IOException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                                else
                                {
                                    event.getHook().sendMessage("Para editar seu cargo, você deve criar o próprio cargo em `/minguantevip criar`!").queue();
                                }


                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }

                        }

                        else
                        {
                            event.getHook().sendMessage("Seu **VIP** ainda não está registrado. Use o comando `/minguantevip criar` para se registrar!").queue();;
                        }
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(command.equals("parceria"))
        {
            String subcommand = event.getSubcommandName();

            if(subcommand.equals("setar"))
            {
                User user = event.getOption("nome").getAsUser();
                Role parceria = event.getGuild().getRoleById("1227990056338194452");
                Role divindade = event.getGuild().getRoleById("1223394386558320680");
                Role helper = event.getGuild().getRoleById("1244410454458110092");

                if(hasRole(member, divindade) || hasRole(member, helper))
                {
                    event.getGuild().addRoleToMember(user, parceria).queue();

                    event.getHook().sendMessage("Cargo de " + parceria.getAsMention() + " atribuído ao membro " + user.getAsMention()).queue();
                }
                else
                {
                    event.getHook().sendMessage("Você não tem permissão para usar este comando!").queue();
                }
            }
        }

    }

    public static boolean hasRole(Member member, Role role)
    {
        List<Role> memberRoles = member.getRoles();
        return memberRoles.contains(role);
    }

    public int hasVip(String memberid)
    {
        try
        {
            String queryCheck = "SELECT idCustomRole, loverIdRole FROM eclipsevip WHERE memberID = ?";
            PreparedStatement checkStatement = ConnectionDB.getConexao().prepareStatement(queryCheck);
            checkStatement.setString(1, memberid);
            ResultSet resultSet = checkStatement.executeQuery();

            String getidCustomRole = null;
            String getloverIdRole = null;
            if(resultSet.next())
            {
                getidCustomRole = resultSet.getString("idCustomRole");
                getloverIdRole = resultSet.getString("loverIdRole");
            }

            if(getidCustomRole == null && getloverIdRole == null)
            {
                return 2; //membro ainda não usou nenhum dos dois comandos
            }

            else if(getloverIdRole != null && getidCustomRole == null)
            {
                return 1; //update no cargo personalizado único do membro
                //isso é no caso da pessoa utilizar o comando /eclipselover antes do /eclipsevip
                //vai ter que ser update porque o membro já estaria no banco de dados
            }

            else if(getloverIdRole == null && getidCustomRole != null)
            {
                return 0; //não posso usar o comando o /eclipsevip de novo
            }

            else if(getloverIdRole != null && getidCustomRole != null)
            {
                return 3;
            }

        }
        catch (SQLException e)
        {
            // TODO: handle exception
            return 0;
        }

        return 4;
    }

    public boolean hasLover(String memberid, String loverid)
    {
        try
        {
            String query = "SELECT memberID, loverId FROM eclipsevip WHERE memberID = ? and loverId = ?";
            PreparedStatement statement = null;
            statement = ConnectionDB.getConexao().prepareStatement(query);

            statement.setString(1, memberid);
            statement.setString(2, loverid);

            ResultSet resultSet = statement.executeQuery(); // Executa a consulta e obtém o resultado

            return resultSet.next();
        }
        catch (SQLException e)
        {
            // TODO: handle exception
            return false;
        }
    }

    // Método para converter imagem usando InputStream e OutputStream
    public static boolean convertImg(InputStream inputImgStream, OutputStream outputImgStream, String formatType) throws IOException
    {
        // Ler a imagem a partir do InputStream
        BufferedImage inputImage = ImageIO.read(inputImgStream);

        // Escrever a imagem convertida no OutputStream
        boolean result = ImageIO.write(inputImage, formatType, outputImgStream);

        // Fechar os streams
        outputImgStream.close();
        inputImgStream.close();

        return result;
    }
    @Override
    public void onGuildReady(GuildReadyEvent event)
    {

        event.getGuild().updateCommands().addCommands(

                Commands.slash("setar", "coloca o cargo de vip em alguém")
                        .addSubcommands(
                                new SubcommandData("eclipsevip", "coloca o cargo Eclipse no usuário")
                                        .addOption(OptionType.USER, "nome", "nome do membro a quem gostaria de setar o vip", true),
                                new SubcommandData("moonvip", "coloca o cargo Lua-Cheia no usuário")
                                        .addOption(OptionType.USER, "nome", "nome do membro a quem gostaria de setar o vip", true),
                                new SubcommandData("minguantevip", "coloca o cargo Minguante no usuário")
                                        .addOption(OptionType.USER, "nome", "nome do membro a quem gostaria de setar o vip", true)
                        ),

                Commands.slash("parceria", "coloca o cargo de parceria em alguém")
                        .addSubcommands(
                                new SubcommandData("setar", "coloca o cargo de parceria no usuário")
                                        .addOption(OptionType.USER, "nome", "nome do membro a quem gostaria de setar o cargo de parceria", true)
                        ),


                Commands.slash("criar", "Comando principal para criar")
                        .addSubcommands(
                                new SubcommandData("eclipsevip", "Cria a sua tag personalizada do VIP Eclipse")
                                        .addOption(OptionType.STRING, "cargo", "Nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Cor em hexadecimal", true),
                                new SubcommandData("eclipselover", "Cria uma tag personalizada para seu amado e você")
                                        .addOption(OptionType.STRING, "cargo", "nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "criar emoji", true)
                                        .addOption(OptionType.STRING, "cor", "cor em hexadecimal da sua preferência. (por exemplo, #ff0000 )", true)
                                        .addOption(OptionType.USER, "nome", "insira o nick para quem gostaria de colocar essa tag", true),
                                new SubcommandData("eclipsefriend", "Cria um cargo personalizado para você e seus amigos")
                                        .addOption(OptionType.STRING, "cargo", "Nome do cargo personalizado", true)
                                        .addOption(OptionType.STRING, "emoji", "Nome do emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Cor do cargo em hexadecimal", true),
                                new SubcommandData("eclipsecall", "cria uma call com o nome que você quiser")
                                        .addOption(OptionType.STRING, "nome", "defina o nome da sua call", true)

                        ),
                Commands.slash("editar", "Comando principal para editar")
                        .addSubcommands(
                                new SubcommandData("eclipsevip", "Edita o cargo personalizado do VIP Eclipse")
                                        .addOption(OptionType.STRING, "cargo", "Novo nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Novo emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Nova cor em hexadecimal", true),
                                new SubcommandData("eclipselover", "remove o lover atual e adiciona em outro membro")
                                        .addOption(OptionType.STRING, "cargo", "novo nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "novo emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "cor em hexadecimal da sua preferência. (por exemplo, #ff0000 )", true)
                                        .addOption(OptionType.USER, "nome", "insira o nick para quem gostaria de colocar essa tag", true),
                                new SubcommandData("eclipsefriend", "edita o cargo de eclipsefriend")
                                        .addOption(OptionType.STRING, "cargo", "Novo nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Novo emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Nova cor em hexadecimal", true)
                        ),

                Commands.slash("remove", "remove alguma informação de algum membro")
                        .addSubcommands(
                                new SubcommandData("eclipsefriend", "remove o cargo de alguém que possui a sua tag")
                                        .addOption(OptionType.USER, "nome", "nome de quem gostaria de remover a tag", true)

                        ),

                Commands.slash("moonvip",  "comandos do VIP Lua-Cheia")
                        .addSubcommands(
                                new SubcommandData("criar", "criar o cargo único do VIP Lua-Cheia")
                                        .addOption(OptionType.STRING, "cargo", "Nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Cor em hexadecimal", true),
                                new SubcommandData("editar", "edita o cargo único do VIP Lua-Cheia")
                                        .addOption(OptionType.STRING, "cargo", "Novo nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Novo emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Nova cor em hexadecimal", true),
                                new SubcommandData("call", "cria uma call com o nome que você quiser")
                                        .addOption(OptionType.STRING, "nome", "defina o nome da sua call", true)
                        ),

                Commands.slash("minguantevip", "comandos do VIP Minguante")
                        .addSubcommands(
                                new SubcommandData("criar", "cria o cargo única para o VIP Minguante")
                                        .addOption(OptionType.STRING, "cargo", "Nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Cor de hexadecimal", true),
                                new SubcommandData("editar", "edita o cargo único para o VIP Minguante")
                                        .addOption(OptionType.STRING, "cargo", "Novo nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Novo emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Nova cor em hexadecimal do cargo", true)
                        ),

                Commands.slash("moonfriend", "comando do VIP Lua-Cheia")
                        .addSubcommands(
                                new SubcommandData("criar", "cria um cargo compartilhável do VIP Lua-Cheia")
                                        .addOption(OptionType.STRING, "cargo", "Nome do cargo personalizado", true)
                                        .addOption(OptionType.STRING, "emoji", "Nome do emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Cor do cargo em hexadecimal", true),
                                new SubcommandData("editar", "edita o cargo compartilhável do VIP Lua-Cheia")
                                        .addOption(OptionType.STRING, "cargo", "Novo nome do cargo", true)
                                        .addOption(OptionType.STRING, "emoji", "Novo emoji do cargo", true)
                                        .addOption(OptionType.STRING, "cor", "Nova cor do cargo em hexadecimal", true),
                                new SubcommandData("give", "dá o cargo moonfriend para outra pessoa")
                                        .addOption(OptionType.USER, "nome", "nome para quem gostaria de dar o cargo", true),
                                new SubcommandData("remove", "remove o cargo de alguém que possui a sua tag")
                                        .addOption(OptionType.USER, "nome", "nome de quem gostaria de remover a tag", true)
                        ),

                Commands.slash("mudae", "comando da mudae")
                        .addSubcommands(
                                new SubcommandData("addquarentena", "aplica quarentena a um membro")
                                        .addOption(OptionType.USER, "nome", "nome do membro para quem gostaria de aplicar quarentena", true),
                                new SubcommandData("removerquarentena", "remove quarentena de um membro")
                                        .addOption(OptionType.USER, "nome", "nome de quem deseja remover a quarentena", true),
                                new SubcommandData("addtimeout", "muta algum membro do canal da mudae")
                                        .addOption(OptionType.USER, "nome", "muta o membro do canal da mudae", true),
                                new SubcommandData("removertimeout", "remove o timeout de algum membro")
                                        .addOption(OptionType.USER, "nome", "remove o timeout do membro", true)
                        ),

                Commands.slash("give", "dá alguma coisa para algum membro do servidor")
                        .addSubcommands(
                                new SubcommandData("eclipsefriend", "dá o cargo para algum membro do servidor")
                                        .addOption(OptionType.USER, "nome", "nome para quem gostaria de dar o cargo", true)
                        ),

                Commands.slash("falar", "digite um texto e o bot irá mandar no chat")
                        .addOption(OptionType.STRING, "texto", "digite o texto que o bot usará no chat", true),

                Commands.slash("contador", "mostra o ranking de bumps")
                        .addSubcommands(
                                new SubcommandData("rank", "mostra o ranking")
                        ),
                Commands.slash("comandos", "instruções de comandos de cada vip")
                        .addSubcommands(
                                new SubcommandData("eclipse", "mostra as instruções de comandos do VIP Eclipse"),
                                new SubcommandData("luacheia", "mostra as instruções de comandos do VIP Lua-Cheia"),
                                new SubcommandData("minguante", "mostra as instruções de comandos do VIP Minguante")
                        )
        ).queue();

    }
}