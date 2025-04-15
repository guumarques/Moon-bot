package Events.Instagram.Buttons;

import Commands.ConnectionDB;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeletePost extends ListenerAdapter
{
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        // Verifica se o botão pressionado é o de "deletar o post"
        if (event.getComponentId().equals("deletar"))
        {
            Message message = event.getMessage();
            String postID = message.getId(); // ID do post é o ID da mensagem
            User userWhoClicked = event.getUser(); // Usuário que clicou no botão

            event.deferReply(true).queue(); // Deferir resposta enquanto processa o comando

            try
            {
                // Consulta no banco de dados para verificar o membro que postou a foto
                String query = "SELECT member_id FROM posts WHERE message_id = ?";
                PreparedStatement statement = ConnectionDB.getInstagramConnection().prepareStatement(query);
                statement.setString(1, postID); // Passa o ID do post para a consulta

                ResultSet rs = statement.executeQuery();

                if (rs.next())
                {
                    // Recupera o member_id do banco de dados
                    String memberIdFromDB = rs.getString("member_id");

                    // Se o usuário que clicou no botão for o mesmo que o armazenado no banco de dados, permite a exclusão
                    if (userWhoClicked.getId().equals(memberIdFromDB))
                    {
                        // Exclui o post do banco de dados
                        String deleteQuery = "DELETE FROM posts WHERE message_id = ?";
                        PreparedStatement deleteStatement = ConnectionDB.getInstagramConnection().prepareStatement(deleteQuery);
                        deleteStatement.setString(1, postID);

                        int rowsAffected = deleteStatement.executeUpdate(); // Executa a exclusão no banco

                        if (rowsAffected > 0)
                        {
                            // Se a exclusão no banco foi bem-sucedida, exclui a mensagem no canal
                            message.delete().queue();

                            // Informa ao usuário que o post foi deletado com sucesso
                            event.getHook().sendMessage("Post deletado com sucesso!").setEphemeral(true).queue();
                        }
                        else
                        {
                            event.getHook().sendMessage("Não foi possível deletar o post. Verifique se o post existe.").setEphemeral(true).queue();
                        }
                    }
                    else
                    {
                        // Se o usuário não é o dono do post, avisa que ele não tem permissão para deletar
                        event.getHook().sendMessage("Você não tem permissão para deletar esse post!").setEphemeral(true).queue();
                    }
                }
                else
                {
                    // Caso não encontre o post no banco de dados
                    event.getHook().sendMessage("Não encontrei esse post no banco de dados.").setEphemeral(true).queue();
                }
            }
            catch (SQLException e)
            {
                // Caso ocorra um erro durante a execução da consulta ou exclusão
                e.printStackTrace();
                event.getHook().sendMessage("Ocorreu um erro ao tentar deletar o post. Tente novamente mais tarde.").setEphemeral(true).queue();
            }
        }
    }
}
