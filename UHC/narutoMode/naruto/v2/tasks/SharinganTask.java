package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kakashi;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SharinganTask extends BukkitRunnable {

    private final UhcHost main;
    private final Kakashi kakashi;

    public SharinganTask(UhcHost main, Kakashi kakashi) {
        this.main = main;
        this.kakashi = kakashi;
    }

    @Override
    public void run() {
        if (kakashi.getCopying() != null) {
            UUID copying = kakashi.getCopying();
            PlayerManager copyingPlayerManager = main.getPlayerManager(copying);
            Player copyingPlayer = Bukkit.getPlayer(copying);
            if (!copyingPlayerManager.isAlive()) {
                kakashi.setCopying(null);
                kakashi.setCopyPoints(0);
            }
            if (kakashi.getPlayer() != null) {
                Player kakashiPlayer = kakashi.getPlayer();

                int pourcent = (int) ((double) kakashi.getCopyPoints() / (double) Kakashi.COPY_POINTS * 100.0D);

                String message = "§7Copie : [";
                message += ChunkLoader.getProgressBar(kakashi.getCopyPoints(), Kakashi.COPY_POINTS, 100, '|', ChatColor.GREEN, ChatColor.WHITE);
                message += "§7] ";
                message += "§a" + kakashi.getCopyPoints() + "§7/§2" + Kakashi.COPY_POINTS + " §7" + pourcent + "%";

                ActionBar.sendMessage(kakashiPlayer, message);
                if (kakashi.getCopyPoints() >= Kakashi.COPY_POINTS) {
                    //FINI SA COPIE
                    kakashi.setCopying(null);
                    kakashi.setActualCopy(copying);
                    kakashi.setCopyPoints(0);
                    kakashi.getCopieds().add(copying);

                    List<PotionEffect> effects = copyingPlayerManager.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).map(e -> {
                        return e.getKey();
                    }).collect(Collectors.toList());

                    kakashiPlayer.getActivePotionEffects().stream().map(e -> {
                        return e.getType();
                    }).forEach(kakashiPlayer::removePotionEffect);

                    effects.forEach(kakashiPlayer::addPotionEffect);
                    kakashiPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez fini votre copie sur " + Bukkit.getOfflinePlayer(copying).getName() + ", vous obtenez donc ses effets !");

                    return;
                }

                if (copyingPlayer != null) {
                    if (kakashiPlayer.getWorld() == copyingPlayer.getWorld()) {
                        double distance = kakashiPlayer.getLocation().distance(copyingPlayer.getLocation());
                        int points = 0;

                        if (distance <= 20 && distance > 10) {
                            points = 2;
                        } else if (distance <= 10 && distance > 5) {
                            points = 5;
                        } else if (distance <= 5) {
                            points = 10;
                        }

                        kakashi.addCopyPoints(points);
                    }
                }
            }
        }
    }
}
