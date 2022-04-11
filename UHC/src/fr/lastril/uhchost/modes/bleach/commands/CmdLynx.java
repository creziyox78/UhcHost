package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.Yoruichi;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdLynx implements ModeSubCommand {

    private final UhcHost main;

    public CmdLynx(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "lynx";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.hasRole() && playerManager.getRole() instanceof Yoruichi) {
            Yoruichi yoruichi = (Yoruichi) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()) {
                if(yoruichi.canTransform()) {
                    if(!yoruichi.isTransformed()) {
                        yoruichi.setTransformed(true);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Vous êtes maintenant un chat aux yeux des autres joueurs !");
                    } else {
                        yoruichi.setTransformed(false);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Vous n'êtes plus un chat aux yeux des autres joueurs !");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Un joueur vous a frappé dans les 2 dernières minutes, vous ne pouvez pas vous transformé en chat !");
                }
            }
        }
        return false;
    }
}
