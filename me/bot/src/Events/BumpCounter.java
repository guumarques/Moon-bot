package Events;

import Commands.ConnectionDB;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BumpCounter extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getChannel().getId().equals("1240342471586873414") && event.getAuthor().getId().equals("735147814878969968"))
        {
            String messageContent = event.getMessage().getContentRaw();

            if(messageContent.startsWith("<:Pinkzuda:1265980650100490252> ∿ Obrigado pelo bump pessoa amável! Iremos te avisar novamente daqui a 2h."))
            {
                if (!event.getMessage().getMentions().getMembers().isEmpty())
                {
                    Member member = event.getMessage().getMentions().getMembers().get(0);

                    TextChannel channel = event.getChannel().asTextChannel();
                    channel.sendMessage("Bump feito por " + member.getEffectiveName()).queue();

                    try
                    {
                        String query = "SELECT contador FROM counter WHERE idMembro = ?";
                        PreparedStatement statement = ConnectionDB.getBumpConnection().prepareStatement(query);

                        statement.setString(1, member.getId());
                        ResultSet rs = statement.executeQuery();

                        if(rs.next())
                        {
                            // Debug: Verificando se o membro já existe no banco de dados
                            System.out.println("Membro encontrado no banco de dados, fazendo UPDATE.");
                            int number = rs.getInt("contador") + 1;

                            String query2 = "UPDATE counter SET contador = ? WHERE idMembro = ?";
                            PreparedStatement statement2 = ConnectionDB.getBumpConnection().prepareStatement(query2);

                            statement2.setInt(1, number);
                            statement2.setString(2, member.getId());

                            int rowsaffected = statement2.executeUpdate();

                            if (rowsaffected > 0) {
                                System.out.println("Contador atualizado com sucesso!");
                            } else {
                                System.out.println("Falha ao atualizar o contador");
                            }

                            statement2.close();
                        }
                        else
                        {
                            // Debug: Verificando se um novo membro está sendo inserido
                            System.out.println("Membro não encontrado, fazendo INSERT.");
                            String query2 = "INSERT INTO counter(idMembro, nome, contador) VALUES(?, ?, 1);";
                            PreparedStatement statement2 = ConnectionDB.getBumpConnection().prepareStatement(query2);

                            statement2.setString(1, member.getId());
                            statement2.setString(2, member.getEffectiveName());

                            int rowsAffected = statement2.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Novo bump do membro inserido com sucesso!");
                            } else {
                                System.out.println("Deu ruim ao inserir o bump");
                            }

                            statement2.close();
                        }

                        rs.close();
                        statement.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
