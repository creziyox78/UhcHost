package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.ZetsuBlanc;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.ZetsuNoir;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdZetsu implements ModeSubCommand {

    private final UhcHost main;

    public CmdZetsu(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive())
            return false;
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof ZetsuNoir) {
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (args.length >= 2) {
                        String message = "";
                        for (int i = 1; i < args.length; i++) {
                            message += args[i] + " ";
                        }
                        player.sendMessage("§8[§c§lZetsu Noir§8] §7" + message);
                        for (PlayerManager targetPlayerManager : main.getNarutoV2Manager().getPlayerManagersWithRole(ZetsuBlanc.class)) {
                            if (targetPlayerManager.isAlive()) {
                                targetPlayerManager.getPlayer().sendMessage("§8[§c§lZetsu Noir§8] §7" + message);
                            }
                        }
                        main.sendMessageToModsInModeration("§8[§c§lZetsu Noir§8] §7" + message);
                        return true;
                    } else {
                        Messages.error("Précisez un message.");
                    }

                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Zetsu Noir"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "zetsu";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

}
