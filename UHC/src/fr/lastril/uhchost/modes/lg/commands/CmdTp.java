package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Trublion;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdTp implements ModeSubCommand {

    private final UhcHost main;

    public CmdTp(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "tp";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.isAlive() && playerManager.hasRole()){
            if(playerManager.getRole() instanceof Trublion){
                Trublion trublion = (Trublion) playerManager.getRole();
                if(!trublion.isTeleported()){
                    trublion.teleportPower(player);
                }  else {
                    player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Trublion"));
            }
        }
        return false;
    }
}
