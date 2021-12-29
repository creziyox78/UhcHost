package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Shino;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackingTask extends BukkitRunnable {

    private final UhcHost main;
    private final Shino shino;

    public TrackingTask(UhcHost main, Shino shino) {
        this.main = main;
        this.shino = shino;
    }


    @Override
    public void run() {
        List<String> transfers = new ArrayList<>();

        if(shino.getPlayer() == null) return;

        Player shinoPlayer = shino.getPlayer();

        for (UUID id : shino.getTrackeds()) {
            Player targetPlayer = Bukkit.getPlayer(id);
            String arrow = targetPlayer == null ? "?" : ClassUtils.getDirectionOf(shinoPlayer.getLocation(), targetPlayer.getLocation());
            transfers.add(targetPlayer != null ? "§7" + targetPlayer.getName() + " : " + arrow + "§7 (" + (int) targetPlayer.getLocation().distance(shinoPlayer.getLocation()) + ")" : "§7" + main.getServer().getOfflinePlayer(id).getName() + " : ?");
        }

        ActionBar.sendMessage(shinoPlayer, Strings.join(transfers, "  "));
    }

}
