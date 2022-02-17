package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

public class Chat implements Listener {

    private final UhcHost main = UhcHost.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(main.getGamemanager().getHost() == player.getUniqueId()){
            event.setFormat("§e§lHost§e " + player.getName() + " §f» " + event.getMessage().replace("&", "§"));
        } else if(main.getGamemanager().isCoHost(player)){
            event.setFormat("§eCo§f-§eHost " + player.getName() + " §f» " + event.getMessage().replace("&", "§"));
        } else if(player.isOp()){
            event.setFormat("§c§lOP " + player.getName() + " §f» " + event.getMessage().replace("&", "§"));
        } else {
            event.setFormat("§7" + player.getName() + " » " + event.getMessage());
        }
        if (this.main.teamUtils.getPlayersPerTeams() != 1) {
            Team t = this.main.teamUtils.getTeam(player);
            if(t != null){
                if (event.getMessage().startsWith("!")) {
                    event.setCancelled(true);
                    event.setFormat( "§8["+t.getPrefix() + "Equipe "+t.getDisplayName() + "§8] "+t.getPrefix() + player.getName() + "§8 »§f " + event.getMessage().substring(1));
                    for(Player players: main.teamUtils.getPlayersInTeam(t)){
                        players.sendMessage(event.getFormat());
                    }
                }
            }

        }
    }

}
