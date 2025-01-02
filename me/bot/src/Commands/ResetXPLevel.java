package Commands;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ResetXPLevel extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        super.onMessageReceived(event);

        // Verifica se o autor da mensagem é um membro válido
        if (event.getMember() == null)
        {
            return;
        }

        Member member = event.getMember();
        Role role = event.getGuild().getRoleById("1223394386558320680");
        var hasDivindadeRole = hasRole(member, role);

        if (event.getMessage().getContentRaw().equals("!resetlevels")) {
            if (hasDivindadeRole) {
                Guild guild = event.getGuild();

                // IDs dos cargos de nível
                Set<String> RolesIDs = new HashSet<>();
                RolesIDs.add("1224040396783620096"); // level 100
                RolesIDs.add("1224040393226719364"); // level 95
                RolesIDs.add("1224040389007380490"); // level 90
                RolesIDs.add("1224040382975836180"); // level 85
                RolesIDs.add("1224040380836876289"); // level 80
                RolesIDs.add("1224040378764755047"); // level 75
                RolesIDs.add("1224040374654206002"); // level 70
                RolesIDs.add("1224040371793694751"); // level 65
                RolesIDs.add("1224040369067524127"); // level 60
                RolesIDs.add("1224040365800165538"); // level 55
                RolesIDs.add("1224040363237572630"); // level 50
                RolesIDs.add("1224040360213221426"); // level 45
                RolesIDs.add("1224040266252685334"); // level 40
                RolesIDs.add("1224040265090732192"); // level 35
                RolesIDs.add("1224040263383781606"); // level 30
                RolesIDs.add("1224040261353607331"); // level 25
                RolesIDs.add("1224040259965288589"); // level 20
                RolesIDs.add("1224040257813614722"); // level 15
                RolesIDs.add("1224040255678578812"); // level 10
                RolesIDs.add("1224040253803855924"); // level 5
                RolesIDs.add("1224040249911410728"); // level 1

                // Itera sobre cada cargo de nível
                for (String roleId : RolesIDs)
                {
                    Role levelRole = guild.getRoleById(roleId);
                    if (levelRole != null)
                    {
                        List<Member> membersWithRole = guild.getMembersWithRoles(levelRole);

                        // Remove o cargo de todos os membros que o possuem
                        for (Member memberWithRole : membersWithRole)
                        {
                            try
                            {
                                guild.removeRoleFromMember(memberWithRole, levelRole).queue();
                            }
                            catch (Exception e)
                            {
                                // Log ou tratamento de erro
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }

                // Responde ao comando com uma mensagem de confirmação
                event.getChannel().sendMessage("**Resetando cargos...**").queue();
            } else {
                event.getChannel().sendMessage("Você não pode executar esse comando").queue();
            }
        }
    }

    public static boolean hasRole(Member member, Role role)
    {
        List<Role> memberRoles = member.getRoles();
        return memberRoles.contains(role);
    }
}
