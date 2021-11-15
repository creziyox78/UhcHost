package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.items.KamuiItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CmdYameru implements ModeSubCommand {

    private final UhcHost main;

    public CmdYameru(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "yameru";
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
            if (PlayerManager.getRole() instanceof KamuiItem.KamuiUser) {
                KamuiItem.KamuiUser kamuiUser = (KamuiItem.KamuiUser) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {

                    for (Map.Entry<UUID, Location> e : kamuiUser.getInitialsLocation().entrySet()) {
                        Player target = Bukkit.getPlayer(e.getKey());
                        if (target != null && target.getWorld() == main.getNarutoV2Manager().getKamuiWorld()) {
                            e.getValue().getChunk().load();
                            target.teleport(e.getValue());
                        }
                    }
                    kamuiUser.getInitialsLocation().clear();
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Tout le monde a été téléporté dans le monde normal.");
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Kakashi ou Obito"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
