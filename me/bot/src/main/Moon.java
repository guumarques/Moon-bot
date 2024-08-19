package main;

import java.sql.SQLException;
import java.util.EnumSet;
import javax.security.auth.login.LoginException;
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
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Moon
{

    public static JDA jda;// estou tornando ele estático para que eu possar acessar de outra classe
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
        jda.addEventListener(new BumpCounter());

        jda.getPresence().setActivity(Activity.customStatus("Cuidando do servidor..."));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }
}