package Commands;

import java.awt.Color;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;

import javax.xml.transform.Result;

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

    @Override
    public void onGuildReady(GuildReadyEvent event)
    {

        event.getGuild().updateCommands().addCommands(
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
                        .addOption(OptionType.STRING, "texto", "digite o texto que o bot usará no chat", true)
        ).queue();

    }
}