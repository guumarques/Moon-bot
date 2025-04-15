package Events.Instagram.Buttons;

import Commands.ConnectionDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SeeComments extends ListenerAdapter
{
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        if(event.getComponentId().equals("vercomentarios"))
        {
            Message message = event.getMessage();
            String postID = message.getId();

            event.deferReply(true).queue();

            try
            {
                String query = "SELECT member_id, comment FROM comments WHERE post_id = ?";
                PreparedStatement statement = ConnectionDB.getInstagramConnection().prepareStatement(query);
                statement.setString(1, postID);

                ResultSet rs = statement.executeQuery();

                HashMap<String, String> comments = new HashMap<>();

                while(rs.next())
                {
                    String member_id = rs.getString("member_id");
                    String comment = rs.getString("comment");

                    comments.put(member_id, comment);
                }

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("<:4763starred:1346664215137615942> Comentários: ");
                embed.setColor(Color.decode("#fccfcc"));
                for(String member_id : comments.keySet())
                {
                    Member member = event.getGuild().getMemberById(member_id);
                    if(member != null)
                    {
                        User user = member.getUser();
                        embed.addField("<:pink_bow:1346678043242004531> " + user.getName(), "<a:dot_pink:1292697945350865038> "+ comments.get(member_id), false);
                    }
                    else
                    {
                        System.out.println("Membro com ID " + member_id + " não encontrado.");
                    }
                }

                event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
