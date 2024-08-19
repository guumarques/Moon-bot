package Commands;

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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
        Category luaCheiaCattegory = guild.getCategoryById("1225962188318179368");
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

                    // ---------------------------------------------------------------------------------------
                    // Define o formato da data e hora
                    SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                    formatar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Define o fuso horário para Brasília

                    // Obtém a data e hora atual e formata
                    Date data = new Date();
                    String dataFormatada = formatar.format(data);

                    // ---------------------------------------------------------------------------------------

                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("●▬▬▬▬▬▬▬▬▬▬๑۩۩๑▬▬▬▬▬▬▬▬▬▬●")
                            .setTitle("<a:vssparkly:1274804830220587140> **Rank Bump** <a:vssparkly:1274804830220587140>")
                            .setDescription(ranking.toString())
                            .setColor(Color.yellow)
                            .setFooter("Bump Ranking - Atualizado em " + dataFormatada);

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

        if(command.equals("mudae"))
        {
            String subcommand = event.getSubcommandName();

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

                    event.reply("Quarentena aplicada permanentemente ao membro " + user.getAsMention()).queue();
                }
                else
                {
                    event.reply("Você não tem acesso a esse comando!").queue();
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

                if(hasRole(member, divindadeRole))
                {
                    if(hasRole(memberzinho, timeoutRole))
                    {
                        event.reply("Usuário em timeout. Não foi possível executar esse comando!").queue();
                    }

                    else
                    {
                        event.getGuild().removeRoleFromMember(user, quarentineRole).queue();
                        event.getGuild().addRoleToMember(user, mudaePlayerRole).queue();

                        event.reply("Quarentena removida do membro " + user.getAsMention()).queue();
                    }
                }
                else
                {
                    event.reply("Você não tem acesso a esse comando!").queue();
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

                if(hasRole(member, divindadeRole))
                {
                    event.getGuild().removeRoleFromMember(user, mudaePlayerRole).queue();
                    event.getGuild().addRoleToMember(user, timeoutRole).queue();

                    event.reply("Timeout aplicado ao membro " + user.getAsMention() + ". Punição registrada no canal <#1223685697794347069>").queue();

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
                    event.reply("Você não tem acesso a esse comando!").queue();
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

                if(hasRole(member, divindadeRole))
                {
                    if(hasRole(memberzinho, quarentineRole))
                    {
                        event.reply("Usuário em quarentena. Não foi possível executar esse comando!").queue();
                    }

                    else
                    {
                        event.getGuild().removeRoleFromMember(user, timeoutRole).queue();
                        event.getGuild().addRoleToMember(user, mudaePlayerRole).queue();

                        event.reply("Timeout removido do membro " + user.getAsMention()).queue();
                    }
                }
                else
                {
                    event.reply("Você não tem acesso a esse comando!").queue();
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
                Commands.slash("remove", "remove alguma informação de algum membro")
                        .addSubcommands(
                                new SubcommandData("eclipsefriend", "remov  e o cargo de alguém que possui a sua tag")
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


                Commands.slash("falar", "digite um texto e o bot irá mandar no chat")
                        .addOption(OptionType.STRING, "texto", "digite o texto que o bot usará no chat", true),

                Commands.slash("contador", "mostra o ranking de bumps")
                        .addSubcommands(
                                new SubcommandData("rank", "mostra o ranking")
                        )
        ).queue();

    }
}