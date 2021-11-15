package fr.lastril.uhchost.modes.naruto.v2.commands.hokage;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdBoost implements ModeSubCommand {

    private final UhcHost main;

    public CmdBoost(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (playerManager.isAlive()) {
            if (main.getNarutoV2Manager().getHokage() == playerManager) {
                if (main.getNarutoV2Manager().getBoosted() == null) {
                    if (args.length >= 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (!target.isOnline()) {
                            player.sendMessage("§cCe PlayerManager n'est pas en ligne.");
                            return false;
                        }
                        PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                        if (!targetPlayerManager.isAlive()) {
                            player.sendMessage(Messages.NOT_INGAME.getMessage());
                            return false;
                        }
                        if (target == player) {
                            player.sendMessage(Messages.NOT_FOR_YOU.getMessage());
                            return false;
                        }
                        main.getNarutoV2Manager().setBoosted(targetPlayerManager);
                        target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eL'Hokage vient de boost votre force et votre résistance.");
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez de boost " + target.getName() + ".");
                        return true;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
                }
            } else {
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous n'êtes pas l'Hokage.");
            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "boost";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
