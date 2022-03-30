package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.IchigoKurosaki;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class Vasto extends QuickItem {

    private int time = 5;
    private BukkitRunnable task;

    public Vasto(UhcHost main) {
        super(Material.SPECKLED_MELON);
        super.setName("§fVasto");
        super.glow(true);
        super.setLore("",
                "§7Permet de faire un \"dash\"",
                "§7sur une distance de 5 blocs",
                "§7Les joueurs touchés perdront 2 coeurs",
                "§7(Cooldown : 3 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof IchigoKurosaki) {
                IchigoKurosaki ichigoKurosaki = (IchigoKurosaki) playerManager.getRole();
                if(ichigoKurosaki.isWhite()) {
                    if(bleachPlayerManager.canUsePower()) {
                        if(playerManager.getRoleCooldownVasto() <= 0) {
                            time = 5;
                            task = (BukkitRunnable) Bukkit.getScheduler().runTaskTimer(main, () -> {
                                player.setVelocity(player.getLocation().getDirection().multiply(1.2D).setY(0));
                                Location location = player.getLocation();
                                location.getWorld().getNearbyEntities(location, 2.0D, 2.0D, 2.0D).forEach(entity -> {
                                    if (entity instanceof Player) {
                                        Player players = (Player)entity;
                                        PlayerManager playerManagers = main.getPlayerManager(players.getUniqueId());
                                        if (playerManagers.isAlive()) {
                                            if (players.getHealth() <= 4.0D) {
                                                players.setHealth(0.0D);
                                            } else {
                                                players.setHealth(players.getHealth() - 4.0D);
                                            }
                                            players.damage(0.0D);
                                            players.setVelocity(players.getLocation().getDirection().multiply(-1).setY(0.8D));
                                        }
                                    }
                                });
                                if (this.time <= 0)
                                    task.cancel();
                                this.time--;
                            }, 0L, 1L);
                            playerManager.setRoleCooldownVasto(60*3);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas sous la forme \"White\" !");
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas \"Ichigo Kurosaki\" !");
            }
        });
    }
}
