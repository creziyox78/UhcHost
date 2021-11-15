package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdReset implements ModeSubCommand {

    private final UhcHost main;

    public CmdReset(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "reset";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
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
            if (PlayerManager.getRole() instanceof ZetsuBlanc) {
                ZetsuBlanc zetsuBlanc = (ZetsuBlanc) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (!zetsuBlanc.hasReset()) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                if (targetPlayerManager.hasRole()) {

                                    targetPlayerManager.clearCooldowns();

                                    player.setMaxHealth(player.getMaxHealth() - (2D * 2D));

                                    zetsuBlanc.setHasReset(true);
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez réinitialisé les délais de " + target.getName() + ".");
                                } else {
                                    player.sendMessage(Messages.NOT_INGAME.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Konan"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }

}
