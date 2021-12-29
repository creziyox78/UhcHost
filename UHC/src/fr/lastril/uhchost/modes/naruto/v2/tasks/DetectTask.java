package fr.lastril.uhchost.modes.naruto.v2.tasks;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class DetectTask extends BukkitRunnable {

    private final UhcHost main;
    private final Madara madara;
    private final UUID detected;

    private int timer = Madara.getDetectionTime();

    public DetectTask(UhcHost main, Madara madara, UUID detected) {
        this.main = main;
        this.madara = madara;
        this.detected = detected;
    }


    @Override
    public void run() {
        if (timer == 0) {
            cancel();
        }

        if (madara.getPlayer() == null) return;

        Player madaraPlayer = madara.getPlayer(), targetPlayer = Bukkit.getPlayer(this.detected);

        String arrow = targetPlayer == null ? "?" : ClassUtils.getDirectionOf(madaraPlayer.getLocation(), targetPlayer.getLocation());

        ActionBar.sendMessage(madaraPlayer, targetPlayer != null ? "§7" + targetPlayer.getName() + " : " + arrow + "§7 (" + (int) targetPlayer.getLocation().distance(madaraPlayer.getLocation()) + ")" : "§7" + main.getServer().getOfflinePlayer(this.detected).getName() + " : ?");


        timer--;
    }
}
