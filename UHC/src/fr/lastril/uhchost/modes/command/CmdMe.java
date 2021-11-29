package fr.lastril.uhchost.modes.command;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdMe implements ModeSubCommand {

    private final UhcHost pl;

    public CmdMe(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "me";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (GameState.isState(GameState.STARTED)) {
                if (pl.gameManager.getModes().getMode() instanceof RoleMode) {
                        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
                        if (playerManager.hasRole()) {
                            playerManager.getRole().sendDescription(player);
                            if (playerManager.getRole() != playerManager.getRole()) {
                                playerManager.getRole().sendDescription(player);
                            }
                        } else {
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
