package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RinneganItem extends QuickItem {

    private final int distance = 50;

    public RinneganItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Rinnegan");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Sasuke) {
                    Sasuke sasuke = (Sasuke) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownRinnegan() <= 0) {
                            for (Entity entity : player.getNearbyEntities(distance, distance, distance)) {
                                if (entity instanceof Player) {
                                    Player players = (Player) entity;
                                    PlayerManager PlayerManagers = main.getPlayerManager(players.getUniqueId());
                                    if (getLookingAt(player, players) && PlayerManagers.isAlive()) {
                                        Location loc1 = player.getLocation();
                                        Location loc2 = players.getLocation();
                                        player.teleport(loc2);
                                        players.teleport(loc1);
                                        players.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§6Sasuke vient d'échanger sa position avec vous.");
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                                        main.getSoundUtils().playSoundDistance(player.getLocation(), 5, "atlantis.rinnegan");
                                        main.getSoundUtils().playSoundDistance(players.getLocation(), 5, "atlantis.rinnegan");
                                        if (player.hasPotionEffect(PotionEffectType.SPEED))
                                            player.removePotionEffect(PotionEffectType.SPEED);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2 * 60, 1, false, false));
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                if (player.hasPotionEffect(PotionEffectType.SPEED))
                                                    player.removePotionEffect(PotionEffectType.SPEED);
                                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                                            }
                                        }.runTaskLater(main, 20 * 2 * 60);
                                        sasuke.usePower(PlayerManager);
                                        PlayerManager.setRoleCooldownRinnegan(5 * 60);
                                        PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownRinnegan(), player.getItemInHand());
                                    }
                                }
                            }

                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownRinnegan()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Sasuke"));
                }
            }
        });
    }

    private boolean getLookingAt(Player player, LivingEntity livingEntity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }

    public interface RinneganUser {

    }

}
