package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Orochimaru;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdMarqueMaudite implements ModeSubCommand {

    private final UhcHost main;

    public CmdMarqueMaudite(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (PlayerManager.isAlive() && PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Orochimaru) {
                Orochimaru orochimaru = (Orochimaru) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        if (orochimaru.getTargetMarque() == null) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                if (target != player) {
                                    PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                    if (targetPlayerManager.isAlive()) {
                                        orochimaru.setTargetMarque(targetPlayerManager);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + target.getName() + " est maintenant imprégné de la §5Marque Maudite.");
                                        target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§5Orochimaru§e vient de vous imprégner la §5Marque Maudite.");
                                    } else {
                                        player.sendMessage(Messages.NOT_INGAME.getMessage());
                                    }
                                } else {
                                    player.sendMessage(Messages.NOT_FOR_YOU.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.NOT_INGAME.getMessage());
                            }
                        } else {
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.CANT_USE_MORE_POWER.getMessage());
                        }
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                }

            } else {
                player.sendMessage(Messages.not("Orochimaru"));
            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "marquemaudite";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
    }

}
