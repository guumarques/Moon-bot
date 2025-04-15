package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import Commands.ConnectionDB;
import Commands.ResetXPLevel;
import Commands.SlashCommands;
import Commands.UserInfo;
import Events.*;
import Events.Instagram.Buttons.*;
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
                new NewRoles(),
                new ChristmasRole(),
                new BirthdayRole(),
                new ParceriaTicket(),
                new TicketEclipse(),
                new TicketLuaCheia(),
                new TicketMinguante(),
                new ResetXPLevel(),
                new mudaeRules(),
                new BumpCounter(),
                new ParceriaCounter(),
                new LikeButton(),
                new CommentButton(),
                new SeeLikes(),
                new SeeComments(),
                new DeletePost()
        );

        jda.getPresence().setActivity(Activity.customStatus("Cuidando do servidor..."));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        Guild guild = jda.getGuildById("1223392724497993778");
        if (guild == null)
        {
            System.err.println("Guild não encontrada!");
            return;
        }

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
            DateTimeFormatter formatar_data = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataAtualFormatada = formatar_data.format(LocalDate.now());

            System.out.println("Verificando vencimento de VIP's...");

            try
            {
                String query = "SELECT data_inicio, data_final, nome, idMembro, tipo_vip FROM mensalidade WHERE  data_final = ?";
                PreparedStatement statement = ConnectionDB.getConexao().prepareStatement(query);

                statement.setString(1, dataAtualFormatada);

                ResultSet rs = statement.executeQuery();
                while (rs.next())
                {
                    String tipo_vip = rs.getString("tipo_vip");
                    String idMembro = rs.getString("idMembro");
                    String nome = rs.getString("nome");
                    Member membro = guild.getMemberById(idMembro);

                    if(tipo_vip.equals("Eclipse"))
                    {
                        System.out.println("O VIP do membro " + nome + " e ID (" + idMembro + ") venceu. Deletando tudo...");

                        if(membro != null) //esse é o caso que o membro está dentro do servidor
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

                                try
                                {
                                    // Verificando e removendo cargos de eclipsevip e eclipselover
                                    String query_3 = "SELECT idCustomRole, loverIdRole FROM eclipsevip WHERE memberID = ?";
                                    PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                    statement3.setString(1, membro.getId());
                                    ResultSet rs3 = statement3.executeQuery();

                                    if (rs3.next())
                                    {
                                        String idCustomRole = rs3.getString("idCustomRole");
                                        String loverIdRole = rs3.getString("loverIdRole");

                                        if (idCustomRole != null)
                                        {
                                            Role customRole = guild.getRoleById(idCustomRole);
                                            customRole.delete().queue();
                                            System.out.println("Cargo " + idCustomRole + " do membro " + membro.getEffectiveName() + " apagado!");
                                        }

                                        if (loverIdRole != null)
                                        {
                                            Role loverRole = guild.getRoleById(loverIdRole);
                                            loverRole.delete().queue();
                                            System.out.println("Cargo " + loverIdRole + " do membro " + membro.getEffectiveName() + " apagado!");
                                        }

                                        // Removendo registros da tabela eclipsevip
                                        String query_4 = "DELETE FROM eclipsevip WHERE memberID = ?";
                                        PreparedStatement statement4 = ConnectionDB.getConexao().prepareStatement(query_4);
                                        statement4.setString(1, membro.getId());
                                        int rowsAffected3 = statement4.executeUpdate();
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
                                    String query_10 = "SELECT createdFriendRoleID, callID FROM eclipsefriend WHERE memberID = ?";
                                    PreparedStatement statement10 = ConnectionDB.getConexao().prepareStatement(query_10);
                                    statement10.setString(1, membro.getId());
                                    ResultSet rs4 = statement10.executeQuery();

                                    if (rs4.next())
                                    {
                                        String createdFriendRoleID = rs4.getString("createdFriendRoleID");
                                        String idCall = rs4.getString("callID");
                                        Role friendRoleID = guild.getRoleById(createdFriendRoleID);

                                        if (friendRoleID != null)
                                        {
                                            friendRoleID.delete().queue();
                                            System.out.println("Cargo " + createdFriendRoleID + " do membro " + membro.getEffectiveName() + " apagado!");
                                        }
                                        if (idCall != null)
                                        {
                                            VoiceChannel call = guild.getVoiceChannelById(idCall);
                                            // Continue com a lógica se `call` não for nulo
                                            if(call != null)
                                            {
                                                call.delete().queue();
                                                System.out.println("Call de nome " + call.getName() + " apagado!");
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("callID está nulo ou vazio para o membro " + membro.getEffectiveName());
                                        }

                                        // Removendo registros da tabela eclipsefriend
                                        String query_6 = "DELETE FROM eclipsefriend WHERE memberID = ?";
                                        PreparedStatement statement6 = ConnectionDB.getConexao().prepareStatement(query_6);
                                        statement6.setString(1, membro.getId());
                                        int rowsAffected3 = statement6.executeUpdate();
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
                                catch (SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                System.out.println("Erro ao remover o membro com VIP vencido do BD!");
                            }


                        }
                        else //esse é o caso em que o membro está fora do servidor, mas seu VIP venceu.
                        {
                            System.out.println("O membro " + nome + " não se encontra no servidor, mas estou apagando tudo!!!");

                            // Removendo membro da tabela mensalidade
                            String query_2 = "DELETE FROM mensalidade WHERE idMembro = ?";
                            PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);
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
                            query_2 = "SELECT idCustomRole, loverIdRole FROM eclipsevip WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);
                            ResultSet rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String idCustomRole = rs2.getString("idCustomRole");
                                String loverIdRole = rs2.getString("loverIdRole");

                                Role customRole = guild.getRoleById(idCustomRole);
                                Role loverRole = guild.getRoleById(loverIdRole);

                                if (customRole != null)
                                {
                                    customRole.delete().queue();
                                    System.out.println("Cargo " + idCustomRole + " do membro " + nome + " apagado!");
                                }

                                if (loverRole != null)
                                {
                                    loverRole.delete().queue();
                                    System.out.println("Cargo " + loverIdRole + " do membro " + nome + " apagado!");
                                }

                                // Removendo registros da tabela eclipsevip
                                String query_3 = "DELETE FROM eclipsevip WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, idMembro);

                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Membro " + nome + " foi apagado do BD eclipsevip e eclipselover devido ao vencimento do vip!");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao apagar o membro " + nome + " do banco de dados eclipsevip e eclipselover!");
                                }
                            }
                            else
                            {
                                System.out.println("Membro " + nome + " não criou o cargo eclipsevip e eclipselover ainda!");
                            }

                            // Verificando e removendo cargos de eclipsefriend
                            query_2 = "SELECT createdFriendRoleID, callID FROM eclipsefriend WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);
                            rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String createdFriendRoleID = rs2.getString("createdFriendRoleID");
                                String callID = rs2.getString("callID");
                                Role friendRoleID = guild.getRoleById(createdFriendRoleID);

                                if (callID != null && !callID.isEmpty())
                                {
                                    VoiceChannel call = guild.getVoiceChannelById(callID);
                                    // Continue com a lógica se `call` não for nulo
                                    if(call != null)
                                    {
                                        call.delete().queue();
                                        System.out.println("Call de nome " + call.getName() + " apagado!");
                                    }
                                }
                                else
                                {
                                    System.out.println("callID está nulo ou vazio para o membro " + membro.getEffectiveName());
                                }
                                if (friendRoleID != null)
                                {
                                    friendRoleID.delete().queue();
                                    System.out.println("Cargo " + createdFriendRoleID + " do membro " + nome + " apagado!");
                                }

                                // Removendo registros da tabela eclipsefriend
                                String query_3 = "DELETE FROM eclipsefriend WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, idMembro);
                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Eclipsefriend do membro " + nome + " foi apagado do BD devido ao vencimento do vip");
                                }
                            }
                            else
                            {
                                System.out.println("O cargo de eclipsefriend do membro " + nome + " não foi criado ainda!");
                            }
                        }
                    }
                    else if(tipo_vip.equals("Lua-Cheia"))
                    {
                        System.out.println("O VIP do membro " + nome + " e ID (" + idMembro + ") venceu. Deletando tudo...");

                        if(membro != null)//aqui é no caso de o membro ainda estar no servidor e o vip dele ter vencido
                        {
                            guild.removeRoleFromMember(membro, luacheia).queue();
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
                            query_2 = "SELECT idCustomRole FROM moonvip WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, membro.getId());
                            ResultSet rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String idCustomRole = rs2.getString("idCustomRole");

                                if (idCustomRole != null)
                                {
                                    Role customRole = guild.getRoleById(idCustomRole);
                                    customRole.delete().queue();
                                    System.out.println("Cargo " + idCustomRole + " do membro " + membro.getEffectiveName() + " apagado!");
                                }

                                // Removendo registros da tabela eclipsevip
                                String query_3 = "DELETE FROM moonvip WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, membro.getId());
                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Membro " + membro.getEffectiveName() + " foi apagado do BD moonvip devido ao vencimento do vip!");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao apagar o membro " + membro.getEffectiveName() + " do banco de dados moonvip!");
                                }
                            }
                            else
                            {
                                System.out.println("Membro " + membro.getEffectiveName() + " não criou o cargo moonvip ainda!");
                            }

                            // Verificando e removendo cargos de eclipsefriend
                            query_2 = "SELECT createdFriendRoleID, callID FROM moonfriend WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, membro.getId());
                            rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String createdFriendRoleID = rs2.getString("createdFriendRoleID");
                                String callID = rs2.getString("callID");
                                Role friendRoleID = guild.getRoleById(createdFriendRoleID);

                                if (friendRoleID != null)
                                {
                                    friendRoleID.delete().queue();
                                    System.out.println("Cargo " + createdFriendRoleID + " do membro " + membro.getEffectiveName() + " apagado!");
                                }
                                if (callID != null)
                                {
                                    VoiceChannel call = guild.getVoiceChannelById(callID);
                                    // Continue com a lógica se `call` não for nulo
                                    if(call != null)
                                    {
                                        call.delete().queue();
                                        System.out.println("Call de nome " + call.getName() + " apagado!");
                                    }
                                }
                                else
                                {
                                    System.out.println("callID está nulo ou vazio para o membro " + membro.getEffectiveName());
                                }

                                // Removendo registros da tabela eclipsefriend
                                String query_3 = "DELETE FROM moonfriend WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, membro.getId());
                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Moonfriend do membro " + membro.getEffectiveName() + " foi apagado do BD devido ao vencimento do vip");
                                }
                            }
                            else
                            {
                                System.out.println("O cargo de moonfriend do membro " + membro.getEffectiveName() + " não foi criado ainda!");
                            }
                        }
                        else //aqui é no caso do vip ter vencido, mas o membro não estar mais no servidor
                        {
                            System.out.println("O membro " + nome + " não se encontra no servidor, mas estou apagando tudo!!!");

                            // Removendo membro da tabela mensalidade
                            String query_2 = "DELETE FROM mensalidade WHERE idMembro = ?";
                            PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);
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
                            query_2 = "SELECT idCustomRole FROM moonvip WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);
                            ResultSet rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String idCustomRole = rs2.getString("idCustomRole");

                                Role customRole = guild.getRoleById(idCustomRole);

                                if (customRole != null)
                                {
                                    customRole.delete().queue();
                                    System.out.println("Cargo " + idCustomRole + " do membro " + nome + " apagado!");
                                }

                                // Removendo registros da tabela eclipsevip
                                String query_3 = "DELETE FROM moonvip WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, idMembro);
                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Membro " + nome + " foi apagado do BD moonvip devido ao vencimento do vip!");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao apagar o membro " + nome + " do banco de dados moonvip!");
                                }
                            }
                            else
                            {
                                System.out.println("Membro " + nome + " não criou o cargo moonvip ainda!");
                            }

                            // Verificando e removendo cargos de eclipsefriend
                            query_2 = "SELECT createdFriendRoleID, callID FROM moonfriend WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);
                            rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String createdFriendRoleID = rs2.getString("createdFriendRoleID");
                                String callID = rs2.getString("callID");
                                Role friendRoleID = guild.getRoleById(createdFriendRoleID);

                                if (callID != null && !callID.isEmpty())
                                {
                                    VoiceChannel call = guild.getVoiceChannelById(callID);
                                    // Continue com a lógica se `call` não for nulo
                                    if(call != null)
                                    {
                                        call.delete().queue();
                                        System.out.println("Call de nome " + call.getName() + " apagado!");
                                    }
                                }
                                else
                                {
                                    System.out.println("callID está nulo ou vazio para o membro " + membro.getEffectiveName());
                                }
                                if (friendRoleID != null)
                                {
                                    friendRoleID.delete().queue();
                                    System.out.println("Cargo " + createdFriendRoleID + " do membro " + nome + " apagado!");
                                }

                                // Removendo registros da tabela eclipsefriend
                                String query_3 = "DELETE FROM moonfriend WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, idMembro);

                                int rowsAffected3 = statement3.executeUpdate();

                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Moonfriend do membro " + nome + " foi apagado do BD devido ao vencimento do vip");
                                }
                            }
                            else
                            {
                                System.out.println("O cargo de moonfriend do membro " + nome + " não foi criado ainda!");
                            }
                        }
                    }
                    else if(tipo_vip.equals("Minguante"))
                    {
                        System.out.println("O VIP do membro " + nome + " e ID (" + idMembro + ") venceu. Deletando tudo...");

                        if(membro != null) //aqui é no caso do membro estar no servidor e o vip dele ter vencido
                        {
                            guild.removeRoleFromMember(membro, minguante).queue();
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
                            query_2 = "SELECT idCustomRole FROM minguantevip WHERE memberID = ?";
                            statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, membro.getId());
                            ResultSet rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String idCustomRole = rs2.getString("idCustomRole");

                                Role customRole = guild.getRoleById(idCustomRole);

                                if (customRole != null)
                                {
                                    customRole.delete().queue();
                                    System.out.println("Cargo " + idCustomRole + " do membro " + membro.getEffectiveName() + " apagado!");
                                }

                                // Removendo registros da tabela eclipsevip
                                String query_3 = "DELETE FROM minguantevip WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, membro.getId());
                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Membro " + membro.getEffectiveName() + " foi apagado do BD minguantevip devido ao vencimento do vip!");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao apagar o membro " + membro.getEffectiveName() + " do banco de dados minguantevip!");
                                }
                            }
                            else
                            {
                                System.out.println("Membro " + membro.getEffectiveName() + " não criou o cargo minguantevip ainda!");
                            }
                        }
                        else //aqui é no caso do membro não estar mais no servidor, mas ainda sim o VIP dele venceu
                        {
                            System.out.println("O membro " + nome + " não se encontra no servidor, mas estou apagando tudo!!!");

                            System.out.println("Cargo removido do membro " + nome + " devido ao vencimento do VIP!");

                            // Removendo membro da tabela mensalidade
                            String query_2 = "DELETE FROM mensalidade WHERE idMembro = ?";
                            PreparedStatement statement2 = ConnectionDB.getConexao().prepareStatement(query_2);
                            statement2.setString(1, idMembro);

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
                            statement2.setString(1, idMembro);
                            ResultSet rs2 = statement2.executeQuery();

                            if (rs2.next())
                            {
                                String idCustomRole = rs2.getString("idCustomRole");

                                Role customRole = guild.getRoleById(idCustomRole);

                                if (customRole != null)
                                {
                                    customRole.delete().queue();
                                    System.out.println("Cargo " + idCustomRole + " do membro " + nome + " apagado!");
                                }

                                // Removendo registros da tabela eclipsevip
                                String query_3 = "DELETE FROM minguantevip WHERE memberID = ?";
                                PreparedStatement statement3 = ConnectionDB.getConexao().prepareStatement(query_3);
                                statement3.setString(1, idMembro);
                                int rowsAffected3 = statement3.executeUpdate();
                                if (rowsAffected3 > 0)
                                {
                                    System.out.println("Membro " + nome + " foi apagado do BD minguantevip devido ao vencimento do vip!");
                                }
                                else
                                {
                                    System.out.println("Ocorreu um erro ao apagar o membro " + nome + " do banco de dados minguantevip!");
                                }
                            }
                            else
                            {
                                System.out.println("Membro " + nome + " não criou o cargo minguantevip ainda!");
                            }
                        }
                    }

                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }, 0, 15, TimeUnit.MINUTES);
    }
}
