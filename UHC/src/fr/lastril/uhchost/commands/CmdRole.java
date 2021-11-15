package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRole implements CommandExecutor {

    public UhcHost main;

    public CmdRole(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (main.gameManager.getGameState() == GameState.STARTED) {
                if (main.gameManager.getModes().getMode() instanceof RoleMode<?>) {
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    if(playerManager.hasRole()) {
                        playerManager.getRole().sendDescription(player);
                        if(playerManager.getRole() != playerManager.getRole()) {
                            player.sendMessage("§8§m----------------------------");
                            playerManager.getRole().sendDescription(player);
                        }
                    }else {
                        player.sendMessage("§cVous n'avez pas rôle !");
                    }
                } else {
                    sender.sendMessage("§cLe mode de jeu configurer ne se joue pas avec des rôles !");
                }
            } else {
                sender.sendMessage("§cLa partie n'est pas lancée !");
            }
        }
        return false;
    }
}
