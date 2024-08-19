package Ruules;

import java.awt.Color;
import main.Moon;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ruules extends ListenerAdapter
{

    @Override
    public void onMessageReceived(MessageReceivedEvent event) 
    {
        // TODO Auto-generated method stub
        super.onMessageReceived(event);

         // TODO Auto-generated method stub
         super.onMessageReceived(event);

         String[] args = event.getMessage().getContentRaw().split(" ");
 
         if(args[0].equalsIgnoreCase("!embed"))
         {
            if(event.getChannel().getIdLong() == 1224853700388982844L)
            {
                
                TextChannel textChannel = (TextChannel)event.getChannel();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                Color minhacor = new Color(255,255,255);

                embedBuilder.setAuthor("Canal de Justificativas de Banimento ou Silenciamento");
                embedBuilder.setImage("https://media.discordapp.net/attachments/1134247700934688818/1224220058910326784/standard_1.gif?ex=661cb2c6&is=660a3dc6&hm=e818e00f13572619ca5237a3ab352ff4690348dd0245e916d8a6333a50429f5a&");
                embedBuilder.setThumbnail(event.getGuild().getIconUrl());
                embedBuilder.setColor(minhacor);
                embedBuilder.setDescription("Este canal tem a finalidade de exibir as punições, assim como as evidências relacionadas.\n\n");
                embedBuilder.addField("Sobre as provas", "Envie capturas de tela das infrações cometidas pelos membros.\n\n**Observação**: É estritamente proibido enviar gravações de vídeo ou áudio, a fim de preservar a reputação dos envolvidos.", false);

                MessageEmbed embed = embedBuilder.build();
                textChannel.sendMessageEmbeds(embed).queue();
            }
        }
    
    }
}
    
