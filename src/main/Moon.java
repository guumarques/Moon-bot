package main;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;

public class Moon 
{

    public static JDA jda;// estou tornando ele estático para que eu possar acessar de outra classe
    public static Map<Long, String> mapGuildName = new HashMap<>(); // isso é para diferenciar os servidores. Ele pega o
                                                                    // ID, do tipo Long e o nome do servidor, do tipo
                                                                    // String
    public static Map<Long, Character> prefixMap = new HashMap<>(); // isso é para o prefixo de cada servidor
    public static Map<Long, Long> JoinChannelMap = new HashMap<>();
    public static Map<Long, Long> LeaveChannelMap = new HashMap<>();
    public static void main(String[] args) throws LoginException, InterruptedException, Exception, SQLException 
    {


        jda = JDABuilder.create("", EnumSet.allOf(GatewayIntent.class)).build();

        jda.addEventListener(new MemberCounter());
        jda.addEventListener(new SlashCommands());
        //jda.addEventListener(new Ruules());
        jda.addEventListener(new UserInfo());
        jda.addEventListener(new Ticket());
        //jda.addEventListener(new EmbedMaker());
        jda.addEventListener(new ParceriaTicket());
        jda.addEventListener(new TicketEclipse());
        jda.addEventListener(new TicketLuaCheia());
        jda.addEventListener(new TicketMinguante());
        jda.addEventListener(new ResetXPLevel());
        jda.addEventListener(new mudaeRules());

        jda.getPresence().setActivity(Activity.customStatus("Cuidando do servidor..."));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        SlashCommands slash = new SlashCommands();
    }
       
    
}