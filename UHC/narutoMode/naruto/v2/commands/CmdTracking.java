package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Shino;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdTracking implements ModeSubCommand {

    private final UhcHost main;

    public CmdTracking(UhcHost main) {
        this.main = main;
    }


    @Override
    public String getSubCommandName() {
        return "tracking";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Shino) {
                Shino shino = (Shino) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                            if (targetPlayerManager.hasRole()) {
                                if (shino.getLinkeds().containsKey(target.getUniqueId())) {
                                    boolean infos = shino.getLinkeds().get(target.getUniqueId());
                                    if (infos) {
                                        shino.getLinkeds().put(target.getUniqueId(), false);
                                        shino.getTrackeds().add(target.getUniqueId());
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous traquez maitenant " + target.getName());
                                    } else {
                                        player.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre commande sur " + target.getName() + " !"));
                                    }
                                } else {
                                    player.sendMessage(Messages.error("Vous n'avez pas infiltré " + target.getName() + " !"));
                                }
                            } else {
                                player.sendMessage(Messages.NOT_INGAME.getMessage());
                            }
                        } else {
                            player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                        }
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Shino"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}