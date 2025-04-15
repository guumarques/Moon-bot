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
import java.util.ArrayList;

public class SeeLikes extends ListenerAdapter
{
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        if(event.getComponentId().equals("vercurtidas"))
        {
            Message message = event.getMessage();
            String postID = message.getId();

            event.deferReply(true).queue();

            try
            {
                String query = "SELECT member_id FROM likes WHERE post_id = ?";
                PreparedStatement statement = ConnectionDB.getInstagramConnection().prepareStatement(query);
                statement.setString(1, postID);

                ResultSet rs = statement.executeQuery();

                ArrayList<String> likes = new ArrayList<>();

                while(rs.next())
                {
                    String member_id = rs.getString("member_id");

                    likes.add(member_id);
                }

                if(likes.isEmpty())
                {
                    event.getHook().sendMessage("Ninguém curtiu esse post!").setEphemeral(true).queue();
                    return; // Sair da função se ninguém curtiu o post
                }

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("<a:heart1:1346667422966616115> Curtido por: ");
                embed.setColor(Color.decode("#fccfcc"));

                for (String getIdIterator : likes)
                {
                    Member membroCurtiu = event.getGuild().getMemberById(getIdIterator);

                    if (membroCurtiu != null)
                    {
                        User user = membroCurtiu.getUser();
                        embed.appendDescription("<a:72193boo:1346664008761217065> " + membroCurtiu.getAsMention() +
                                " <a:prettyarrowR:1344078943984025651> " + user.getName() + "\n");
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
