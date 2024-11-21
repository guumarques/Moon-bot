package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import Commands.ConnectionDB;
import Commands.ResetXPLevel;
import Commands.SlashCommands;
import Commands.UserInfo;
import Events.BumpCounter;
import Events.MemberCounter;
import Events.mudaeRules;
import Events.Ticket.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import Ruules.Ruules;
import EmbedMaker.EmbedMaker;

public class Moon {

    public static JDA jda; // Tornando estático para acessar de outra classe
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws LoginException, InterruptedException, Exception, SQLException {
        jda = JDABuilder.create("", EnumSet.allOf(GatewayIntent.class))
                .build().awaitReady();

        jda.addEventListener(
                new MemberCounter(),
                new SlashCommands(),
                new Ruules(),
                new UserInfo(),
                new Ticket(),
                new EmbedMaker(),
                new ParceriaTicket(),
                new TicketEclipse(),
                new TicketLuaCheia(),
                new TicketMinguante(),
                new ResetXPLevel(),
                new mudaeRules(),
                new BumpCounter()
        );

        jda.getPresence().setActivity(Activity.customStatus("Cuidando do servidor..."));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        Guild guild = jda.getGuildById("1223392724497993778");
        if (guild == null)
        {
            System.err.println("Guild não encontrada!");
            return;
        }

	//cargos
        Role eclipse = guild.getRoleById("1223689600321454153");
        Role luacheia = guild.getRoleById("1225963752076083231");
        Role minguante = guild.getRoleById("1225963823815458948");
        if (eclipse == null)
        {
            System.err.println("Role não encontrada!");
            return;
        }
        if (luacheia == null)
        {
            System.err.println("Role não encontrada!");
            return;
        }
        if (minguante == null)
        {
            System.err.println("Role não encontrada!");
            return;
        }

        // Agendando a verificação periódica
        scheduler.scheduleAtFixedRate(() ->
        {
            List<Member> membrosComEclipse = guild.getMembersWithRoles(eclipse);
            List<Member> membrosComLuaCheia = guild.getMembersWithRoles(luacheia);
            List<Member> membrosComMinguante = guild.getMembersWithRoles(minguante);
            DateTimeFormatter formatar_data = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataAtualFormatada = formatar_data.format(LocalDate.now());

            System.out.println("Verificando vencimento de VIP's...");

            for (Member membro : membrosComEclipse)
            {

                try
                {
                    String query = "SELECT data_inicio, data_final, nome, idMembro, tipo_vip FROM mensalidade WHERE idMembro = ? AND data_final = ?";
                    PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                    statement.setString(1, membro.getId());
                    statement.setString(2, dataAtualFormatada);

                    ResultSet rs = statement.executeQuery();

                    if (rs.next())
                    {
                        guild.removeRoleFromMember(membro, eclipse).queue();
                        System.out.println("Cargo removido do membro " + membro.getEffectiveName() + " devido ao vencimento do VIP!");

                        // Removendo membro da tabela mensalidade
                        String query_2 = "DELETE FROM mensalidade WHERE idMembro = ?";
                        PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membro.getId());
                        int rowsAffected = statement2.executeUpdate();
                        if (rowsAffected > 0)
                        {
                            System.out.println("Membro com VIP vencido foi removido do BD!");
                        }
                        else
                        {
                            System.out.println("Erro ao remover o membro com VIP vencido do BD!");
                        }

                        // Verificando e removendo cargos de eclipsevip e eclipselover
                        query_2 = "SELECT idCustomRole, loverIdRole, callID FROM eclipsevip WHERE memberID = ?";
                        statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membro.getId());
                        ResultSet rs2 = statement2.executeQuery();

                        if (rs2.next())
                        {
                            String idCustomRole = rs2.getString("idCustomRole");
                            String loverIdRole = rs2.getString("loverIdRole");
                            String callID = rs2.getString("callID");

                            Role customRole = guild.getRoleById(idCustomRole);
                            Role loverRole = guild.getRoleById(loverIdRole);
                            VoiceChannel call = guild.getVoiceChannelById(callID);

                            if (customRole != null)
                            {
                                customRole.delete().queue();
                                System.out.println("Cargo " + idCustomRole + " do membro " + membro.getEffectiveName() + " apagado!");
                            }

                            if (loverRole != null)
                            {
                                loverRole.delete().queue();
                                System.out.println("Cargo " + loverIdRole + " do membro " + membro.getEffectiveName() + " apagado!");
                            }

                            if(call != null)
                            {
                                call.delete().queue();
                                System.out.println("Call de nome " + call.getName() + " apagado!");
                            }

                            // Removendo registros da tabela eclipsevip
                            String query_3 = "DELETE FROM eclipsevip WHERE memberID = ?";
                            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                            statement3.setString(1, membro.getId());
                            int rowsAffected3 = statement3.executeUpdate();
                            if (rowsAffected3 > 0)
                            {
                                System.out.println("Membro " + membro.getEffectiveName() + " foi apagado do BD eclipsevip e eclipselover devido ao vencimento do vip!");
                            }
                            else
                            {
                                System.out.println("Ocorreu um erro ao apagar o membro " + membro.getEffectiveName() + " do banco de dados eclipsevip e eclipselover!");
                            }
                        }
                        else
                        {
                            System.out.println("Membro " + membro.getEffectiveName() + " não criou o cargo eclipsevip e eclipselover ainda!");
                        }

                        // Verificando e removendo cargos de eclipsefriend
                        query_2 = "SELECT createdFriendRoleID FROM eclipsefriend WHERE memberID = ?";
                        statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membro.getId());
                        rs2 = statement2.executeQuery();

                        if (rs2.next())
                        {
                            String createdFriendRoleID = rs2.getString("createdFriendRoleID");
                            Role friendRoleID = guild.getRoleById(createdFriendRoleID);

                            if (friendRoleID != null)
                            {
                                friendRoleID.delete().queue();
                                System.out.println("Cargo " + createdFriendRoleID + " do membro " + membro.getEffectiveName() + " apagado!");
                            }

                            // Removendo registros da tabela eclipsefriend
                            String query_3 = "DELETE FROM eclipsefriend WHERE memberID = ?";
                            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                            statement3.setString(1, membro.getId());
                            int rowsAffected3 = statement3.executeUpdate();
                            if (rowsAffected3 > 0)
                            {
                                System.out.println("Eclipsefriend do membro " + membro.getEffectiveName() + " foi apagado do BD devido ao vencimento do vip");
                            }
                        }
                        else
                        {
                            System.out.println("O cargo de eclipsefriend do membro " + membro.getEffectiveName() + " não foi criado ainda!");
                        }

                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            for (Member membroluacheia : membrosComLuaCheia)
            {

                try
                {
                    String query = "SELECT data_inicio, data_final, nome, idMembro, tipo_vip FROM mensalidade WHERE idMembro = ? AND data_final = ?";
                    PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                    statement.setString(1, membroluacheia.getId());
                    statement.setString(2, dataAtualFormatada);

                    ResultSet rs = statement.executeQuery();

                    if (rs.next())
                    {
                        guild.removeRoleFromMember(membroluacheia, luacheia).queue();
                        System.out.println("Cargo removido do membro " + membroluacheia.getEffectiveName() + " devido ao vencimento do VIP!");

                        // Removendo membro da tabela mensalidade
                        String query_2 = "DELETE FROM mensalidade WHERE idMembro = ?";
                        PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membroluacheia.getId());
                        int rowsAffected = statement2.executeUpdate();
                        if (rowsAffected > 0)
                        {
                            System.out.println("Membro com VIP vencido foi removido do BD!");
                        }
                        else
                        {
                            System.out.println("Erro ao remover o membro com VIP vencido do BD!");
                        }

                        // Verificando e removendo cargos de eclipsevip e eclipselover
                        query_2 = "SELECT idCustomRole, callID FROM moonvip WHERE memberID = ?";
                        statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membroluacheia.getId());
                        ResultSet rs2 = statement2.executeQuery();

                        if (rs2.next())
                        {
                            String idCustomRole = rs2.getString("idCustomRole");
                            String callID = rs2.getString("callID");

                            Role customRole = guild.getRoleById(idCustomRole);
                            VoiceChannel call = guild.getVoiceChannelById(callID);

                            if (customRole != null)
                            {
                                customRole.delete().queue();
                                System.out.println("Cargo " + idCustomRole + " do membro " + membroluacheia.getEffectiveName() + " apagado!");
                            }

                            if(call != null)
                            {
                                call.delete().queue();
                                System.out.println("Call de nome " + call.getName() + " apagado!");
                            }

                            // Removendo registros da tabela eclipsevip
                            String query_3 = "DELETE FROM moonvip WHERE memberID = ?";
                            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                            statement3.setString(1, membroluacheia.getId());
                            int rowsAffected3 = statement3.executeUpdate();
                            if (rowsAffected3 > 0)
                            {
                                System.out.println("Membro " + membroluacheia.getEffectiveName() + " foi apagado do BD moonvip devido ao vencimento do vip!");
                            }
                            else
                            {
                                System.out.println("Ocorreu um erro ao apagar o membro " + membroluacheia.getEffectiveName() + " do banco de dados moonvip!");
                            }
                        }
                        else
                        {
                            System.out.println("Membro " + membroluacheia.getEffectiveName() + " não criou o cargo moonvip ainda!");
                        }

                        // Verificando e removendo cargos de eclipsefriend
                        query_2 = "SELECT createdFriendRoleID FROM moonfriend WHERE memberID = ?";
                        statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membroluacheia.getId());
                        rs2 = statement2.executeQuery();

                        if (rs2.next())
                        {
                            String createdFriendRoleID = rs2.getString("createdFriendRoleID");
                            Role friendRoleID = guild.getRoleById(createdFriendRoleID);

                            if (friendRoleID != null)
                            {
                                friendRoleID.delete().queue();
                                System.out.println("Cargo " + createdFriendRoleID + " do membro " + membroluacheia.getEffectiveName() + " apagado!");
                            }

                            // Removendo registros da tabela eclipsefriend
                            String query_3 = "DELETE FROM moonfriend WHERE memberID = ?";
                            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                            statement3.setString(1, membroluacheia.getId());
                            int rowsAffected3 = statement3.executeUpdate();
                            if (rowsAffected3 > 0)
                            {
                                System.out.println("Moonfriend do membro " + membroluacheia.getEffectiveName() + " foi apagado do BD devido ao vencimento do vip");
                            }
                        }
                        else
                        {
                            System.out.println("O cargo de moonfriend do membro " + membroluacheia.getEffectiveName() + " não foi criado ainda!");
                        }

                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            for (Member membrominguante : membrosComMinguante)
            {

                try
                {
                    String query = "SELECT data_inicio, data_final, nome, idMembro, tipo_vip FROM mensalidade WHERE idMembro = ? AND data_final = ?";
                    PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                    statement.setString(1, membrominguante.getId());
                    statement.setString(2, dataAtualFormatada);

                    ResultSet rs = statement.executeQuery();

                    if (rs.next())
                    {
                        guild.removeRoleFromMember(membrominguante, minguante).queue();
                        System.out.println("Cargo removido do membro " + membrominguante.getEffectiveName() + " devido ao vencimento do VIP!");

                        // Removendo membro da tabela mensalidade
                        String query_2 = "DELETE FROM mensalidade WHERE idMembro = ?";
                        PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membrominguante.getId());
                        int rowsAffected = statement2.executeUpdate();
                        if (rowsAffected > 0)
                        {
                            System.out.println("Membro com VIP vencido foi removido do BD!");
                        }
                        else
                        {
                            System.out.println("Erro ao remover o membro com VIP vencido do BD!");
                        }

                        // Verificando e removendo cargos de eclipsevip e eclipselover
                        query_2 = "SELECT idCustomRole FROM minguantevip WHERE memberID = ?";
                        statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                        statement2.setString(1, membrominguante.getId());
                        ResultSet rs2 = statement2.executeQuery();

                        if (rs2.next())
                        {
                            String idCustomRole = rs2.getString("idCustomRole");

                            Role customRole = guild.getRoleById(idCustomRole);

                            if (customRole != null)
                            {
                                customRole.delete().queue();
                                System.out.println("Cargo " + idCustomRole + " do membro " + membrominguante.getEffectiveName() + " apagado!");
                            }

                            // Removendo registros da tabela eclipsevip
                            String query_3 = "DELETE FROM minguantevip WHERE memberID = ?";
                            PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                            statement3.setString(1, membrominguante.getId());
                            int rowsAffected3 = statement3.executeUpdate();
                            if (rowsAffected3 > 0)
                            {
                                System.out.println("Membro " + membrominguante.getEffectiveName() + " foi apagado do BD minguantevip devido ao vencimento do vip!");
                            }
                            else
                            {
                                System.out.println("Ocorreu um erro ao apagar o membro " + membrominguante.getEffectiveName() + " do banco de dados minguantevip!");
                            }
                        }
                        else
                        {
                            System.out.println("Membro " + membrominguante.getEffectiveName() + " não criou o cargo minguantevip ainda!");
                        }

                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }, 0, 15, TimeUnit.MINUTES);
    }
}
