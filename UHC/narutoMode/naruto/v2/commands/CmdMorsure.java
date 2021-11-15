package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Karin;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdMorsure implements ModeSubCommand {

    private final UhcHost main;

    public CmdMorsure(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive())
            return false;
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Karin) {
                Karin karin = (Karin) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                            if (targetPlayerManager.isAlive()) {
                                if (karin.isUseMorsure()) {
                                    if (player.getMaxHealth() - 2D <= 0) {
                                        player.sendMessage(Messages.error("Vous n'avez plus assez de coeur permanent."));
                                        return false;
                                    }
                                    player.setMaxHealth(player.getMaxHealth() - 2D);
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous avez utilisé votre pouvoir 1 fois cette épisode. Vous perdez 1 coeur permanent.");
                                }
                                target.setHealth(target.getMaxHealth());
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous venez de soigner "
                                        + target.getName());
                                target.sendMessage(
                                        Messages.NARUTO_PREFIX.getMessage() + "§6Karin vient de vous soigner.");
                                karin.setUseMorsure(true);
                            }
                        } else {
                            player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                        }
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "morsure";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
