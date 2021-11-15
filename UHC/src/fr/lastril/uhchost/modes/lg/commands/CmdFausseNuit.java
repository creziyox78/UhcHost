package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarouLunaire;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CmdFausseNuit implements ModeSubCommand {

    private final UhcHost pl;

    public CmdFausseNuit(UhcHost pl) {
        this.pl = pl;
    }


    @Override
    public String getSubCommandName() {
        return "fs";
    }

    @Override
    public List<String> getSubArgs() {
        return Collections.singletonList("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if (playerManager.getRole() instanceof LoupGarouLunaire) {
            LoupGarouLunaire loupGarouLunaire = (LoupGarouLunaire) playerManager.getRole();
            if (!loupGarouLunaire.isUsedFausseNuit() && !loupGarouLunaire.isUseFausseNuit()) {
                Bukkit.getWorld("game").setTime(12999);
                Bukkit.broadcastMessage("§9Le Loup-Garou Lunaire a décider de remettre la nuit.");
                loupGarouLunaire.setUsedFausseNuit(true);
            }
        }
        return false;
    }
}
