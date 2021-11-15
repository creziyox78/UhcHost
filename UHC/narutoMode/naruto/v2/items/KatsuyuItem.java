package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Sakura;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Tsunade;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KatsuyuItem extends QuickItem {

    private static final int DISTANCE = 30;

    public KatsuyuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bKastsuyu");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Tsunade || PlayerManager.getRole() instanceof Sakura) {
                    if (!main.getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownKatsuyu() == 0) {
                            if (playerClick.hasPotionEffect(PotionEffectType.REGENERATION)) {
                                playerClick.removePotionEffect(PotionEffectType.REGENERATION);
                            }
                            playerClick.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 2, false, false));
                            for (Entity entity : playerClick.getNearbyEntities(DISTANCE, DISTANCE, DISTANCE)) {
                                if (entity instanceof Player) {
                                    Player nearPlayer = (Player) entity;
                                    if (nearPlayer.getGameMode() != GameMode.SPECTATOR) {
                                        if (nearPlayer.hasPotionEffect(PotionEffectType.REGENERATION)) {
                                            nearPlayer.removePotionEffect(PotionEffectType.REGENERATION);
                                        }
                                        nearPlayer.addPotionEffect(
                                                new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 2, false, false));
                                    }
                                }
                            }
                            if (playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                                playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    playerClick.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,
                                            Integer.MAX_VALUE, 0, false, false));
                                }
                            }.runTaskLater(main, 20 * 60 * 5);

                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownKatsuyu(20 * 60);
                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                narutoRole.usePower(PlayerManager);
                            }
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownKatsuyu(), playerClick.getItemInHand());
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownKatsuyu()));
                        }
                    } else {
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    playerClick.sendMessage(Messages.not("Tsunade ou Sakura"));
                    return;
                }
            } else {
                playerClick.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
