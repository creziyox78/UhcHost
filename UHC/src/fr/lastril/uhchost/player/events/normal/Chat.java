package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
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
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(!playerManager.isAlive() && GameState.isState(GameState.STARTED)){
            main.getPlayerManagerOnlines().forEach(specManager -> {
                if(!specManager.isAlive()){
                    if(specManager.getPlayer() != null){
                        specManager.getPlayer().sendMessage(Messages.SPECTATOR_PREFIX.getMessage() + player.getName() + " » " + event.getMessage());
                    }
                }
            });
            UhcHost.debug(Messages.SPECTATOR_PREFIX.getMessage() + player.getName() + " » " + event.getMessage());
            event.setCancelled(true);
            return;
        }
        if(main.getGamemanager().getHost() == player.getUniqueId()){
            event.setFormat("§e§lHost§e " + player.getName() + " §f» " + event.getMessage().replace("&", "§"));
        } else if(main.getGamemanager().isCoHost(player)){
            event.setFormat("§eCo§f-§eHost " + player.getName() + " §f» " + event.getMessage().replace("&", "§"));
        } else if(player.isOp()){
            event.setFormat("§c§lOP " + player.getName() + " §f» " + event.getMessage().replace("&", "§"));
        } else {
            event.setFormat("§7" + player.getName() + " » " + event.getMessage());
        }
        if (this.main.teamUtils.getPlayersPerTeams() != 1 || this.main.getGamemanager().getModes() == Modes.SM) {
            Team t = this.main.teamUtils.getTeam(player);
            if(t != null){
                if (!event.getMessage().startsWith("!")) {
                    event.setCancelled(true);
                    event.setFormat( "§8["+t.getPrefix() + "Equipe "+t.getDisplayName() + "§8] "+t.getPrefix() + player.getName() + "§8 »§f " + event.getMessage());
                    for(Player players: main.teamUtils.getPlayersInTeam(t)){
                        players.sendMessage(event.getFormat());
                    }
                } else {
                    if(main.getGamemanager().getHost() == player.getUniqueId()){
                        event.setFormat("§e§lHost§e " + player.getName() + " §f» " + event.getMessage().replace("&", "§").substring(1));
                    } else if(main.getGamemanager().isCoHost(player)){
                        event.setFormat("§eCo§f-§eHost " + player.getName() + " §f» " + event.getMessage().replace("&", "§").substring(1));
                    } else if(player.isOp()){
                        event.setFormat("§c§lOP " + player.getName() + " §f» " + event.getMessage().replace("&", "§").substring(1));
                    } else {
                        event.setFormat("§7" + player.getName() + " » " + event.getMessage().substring(1));
                    }
                }
            }
            if(main.getGamemanager().getModes() == Modes.TAUPEGUN){
                Team taupe = this.main.teamUtils.getTeamTaupe(player);
                if(taupe != null){
                    if (!event.getMessage().startsWith("!")) {
                        event.setCancelled(true);
                        event.setFormat( "§8["+taupe.getPrefix() + "Equipe "+taupe.getDisplayName() + "§8] "+taupe.getPrefix() + player.getName() + "§8 »§f " + event.getMessage());
                        for(Player players: main.teamUtils.getPlayersInTeam(taupe)){
                            players.sendMessage(event.getFormat());
                        }
                    } else {
                        if(main.getGamemanager().getHost() == player.getUniqueId()){
                            event.setFormat("§e§lHost§e " + player.getName() + " §f» " + event.getMessage().replace("&", "§").substring(1));
                        } else if(main.getGamemanager().isCoHost(player)){
                            event.setFormat("§eCo§f-§eHost " + player.getName() + " §f» " + event.getMessage().replace("&", "§").substring(1));
                        } else if(player.isOp()){
                            event.setFormat("§c§lOP " + player.getName() + " §f» " + event.getMessage().replace("&", "§").substring(1));
                        } else {
                            event.setFormat("§7" + player.getName() + " » " + event.getMessage().substring(1));
                        }
                    }
                }
            }

        }
    }

}
