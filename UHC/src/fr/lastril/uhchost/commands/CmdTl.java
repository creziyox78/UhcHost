package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.Modes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class CmdTl implements CommandExecutor {

    private final UhcHost main = UhcHost.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if (this.main.teamUtils.getPlayersPerTeams() != 1) {
                Team t = this.main.teamUtils.getTeam(player);
                if(t != null){
                    for(Player players: main.teamUtils.getPlayersInTeam(t)){
                        int x = player.getLocation().getBlockX();
                        int y = player.getLocation().getBlockY();
                        int z = player.getLocation().getBlockZ();
                        players.sendMessage("§bCoordonnées de " +player.getName()+ "§8 >> §bx:"+ x +" §8|§b  y:"+y+"  §8|§b  z:"+z+".");
                    }
                }
                if(main.getGamemanager().getModes() == Modes.TAUPEGUN){
                    Team taupes = this.main.teamUtils.getTeamTaupe(player);
                    if(taupes != null){
                        for(Player players: main.teamUtils.getPlayersInTeam(taupes)){
                            int x = player.getLocation().getBlockX();
                            int y = player.getLocation().getBlockY();
                            int z = player.getLocation().getBlockZ();
                            players.sendMessage("§bCoordonnées de " +player.getName()+ "§8 >> §bx:"+ x +" §8|§b  y:"+y+"  §8|§b  z:"+z+".");
                        }
                    }
                }
            }
        }
        return false;
    }

}
