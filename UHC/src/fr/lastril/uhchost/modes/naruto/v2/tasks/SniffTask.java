package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Kiba;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SniffTask extends BukkitRunnable {

    private final UhcHost main;
    private final Kiba kiba;
    private final UUID sniffed;

    public SniffTask(UhcHost main, Kiba kiba, UUID sniffed) {
        this.main = main;
        this.kiba = kiba;
        this.sniffed = sniffed;
    }


    @Override
    public void run() {

        if(kiba.getPlayer() == null) return;

        Player kibaPlayer = kiba.getPlayer(), targetPlayer = Bukkit.getPlayer(this.sniffed);

        if(!main.getPlayerManager(kibaPlayer.getUniqueId()).isAlive() || !main.getPlayerManager(targetPlayer.getUniqueId()).isAlive()){
            cancel();
        }

        String arrow = targetPlayer == null ? "?" : ClassUtils.getDirectionOf(kibaPlayer.getLocation(), targetPlayer.getLocation());

        ActionBar.sendMessage(kibaPlayer, targetPlayer != null ? "§7" + targetPlayer.getName() + " : " + arrow + "§7 (" + (int) targetPlayer.getLocation().distance(kibaPlayer.getLocation()) + ")" : "§7" + main.getServer().getOfflinePlayer(this.sniffed).getName() + " : ?");
    }
}
