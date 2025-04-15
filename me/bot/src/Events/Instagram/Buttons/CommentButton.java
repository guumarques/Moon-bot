package Events.Instagram.Buttons;

import Commands.ConnectionDB;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentButton extends ListenerAdapter
{
    private final Map<String, ButtonInteractionEvent> buttonEvents = new HashMap<>(); //usando para guardar informações do botão depois que responder o formulário

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {

        if(event.getComponentId().equals("comentar"))
        {
            // Armazena o evento do botão
            buttonEvents.put(event.getUser().getId(), event);

            TextInput commentInput = TextInput.create("comentario", "Digite seu comentário", TextInputStyle.PARAGRAPH).build();

            Modal modal = Modal.create("modal_comentario", "Comentários")
                    .addActionRow(commentInput)
                    .build();

            event.replyModal(modal).queue();
        }
    }

    private int updateComments(Long postId, String memberID, String comment)
    {
        try
        {
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = data.format(new Date());

            String insertQuery = "INSERT INTO comments (post_id, member_id, comment, date_commented) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = ConnectionDB.getInstagramConnection().prepareStatement(insertQuery);
            insertStatement.setLong(1, postId);
            insertStatement.setString(2, memberID);
            insertStatement.setString(3, comment);
            insertStatement.setString(4, dataFormatada);
            insertStatement.executeUpdate();

            // Agora apenas retorna o número total de comentários do post
            String countQuery = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
            PreparedStatement countStatement = ConnectionDB.getInstagramConnection().prepareStatement(countQuery);
            countStatement.setLong(1, postId);
            ResultSet countResult = countStatement.executeQuery();

            if (countResult.next())
            {
                return countResult.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1; // Se ocorrer erro
    }


    private void updateButton(ButtonInteractionEvent event, int commentCount)
    {
        Button newCommentButton = Button.secondary("comentar", String.valueOf(commentCount)).withEmoji(Emoji.fromUnicode("💬"));
        event.editButton(newCommentButton).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event)
    {
        if(event.getModalId().equals("modal_comentario"))
        {
            Message message = event.getMessage();
            Member member = event.getMember();
            String memberID = member.getId();
            long postID = message.getIdLong();
            String comentario = event.getValue("comentario").getAsString();

            event.deferReply(true).queue();
            event.getHook().sendMessage("Comentário postado com sucesso!").queue();

            int contadorComments = updateComments(postID, memberID, comentario);
            //recupera o evento do botão salvo.
            ButtonInteractionEvent buttonEvent = buttonEvents.get(memberID);
            if(buttonEvent != null)
            {
                updateButton(buttonEvent, contadorComments);
                buttonEvents.remove(memberID);
            }
        }
    }
}
