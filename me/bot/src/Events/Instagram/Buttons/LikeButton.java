package Events.Instagram.Buttons;

import Commands.ConnectionDB;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeButton extends ListenerAdapter
{
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        if(event.getComponentId().equals("curtir"))
        {
            Message message = event.getMessage();
            Member member = event.getMember();
            String memberID = member.getId();
            long postID = message.getIdLong();

            int contadorLikes = updateLikes(postID, memberID); //atualizo o contador no bd
            updateButton(event, contadorLikes); //atualizo o botão
        }
    }

    private int updateLikes(Long postId, String memberID)
    {
        try
        {
            String query = "SELECT COUNT(*) FROM likes WHERE post_id = ? AND member_id = ?"; //verifica se o membro ja curtiu o post
            PreparedStatement statement = ConnectionDB.getInstagramConnection().prepareStatement(query);

            statement.setLong(1, postId);
            statement.setString(2, memberID);

            ResultSet resultSet = statement.executeQuery(); // Executa a consulta e obtém o resultado
            if(resultSet.next() && resultSet.getInt(1) > 0) //se já curtiu,remove a curtida
            {
                String deleteQuery = "DELETE FROM likes WHERE post_id = ? AND member_id = ?";
                PreparedStatement deleteStatement = ConnectionDB.getInstagramConnection().prepareStatement(deleteQuery);
                deleteStatement.setLong(1, postId);
                deleteStatement.setString(2, memberID);
                deleteStatement.executeUpdate();
            }
            else //ainda não curtiu o post, então adiciona bd
            {
                String insertQuery = "INSERT INTO likes (post_id, member_id) VALUES (?, ?)";
                PreparedStatement insertStatement = ConnectionDB.getInstagramConnection().prepareStatement(insertQuery);
                insertStatement.setLong(1, postId);
                insertStatement.setString(2, memberID);
                insertStatement.executeUpdate();
            }

            String countQuery = "SELECT COUNT(*) FROM likes WHERE post_id = ?";
            PreparedStatement countStatement = ConnectionDB.getInstagramConnection().prepareStatement(countQuery);
            countStatement.setLong(1, postId);
            ResultSet countResultSet = countStatement.executeQuery();
            if(countResultSet.next())
            {
                return countResultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1; //se ocorrer algum erro
    }
    private void updateButton(ButtonInteractionEvent event, int likeCount)
    {
        Button newLikeButton = Button.secondary("curtir", String.valueOf(likeCount))

                .withEmoji(Emoji.fromUnicode("❤\uFE0F"));

        event.editButton(newLikeButton).queue();
    }
}
