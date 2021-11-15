package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.api.particles.DoubleCircleEffect;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.HuitPorteUser;
import fr.maygo.uhc.obj.PlayerManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HuitPortesItem extends QuickItem {

    public HuitPortesItem() {
        super(Material.NETHER_STAR);
        super.setName("§2Huit portes");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (PlayerManager.getRole() instanceof HuitPorteUser) {
                    HuitPorteUser user = (HuitPorteUser) PlayerManager.getRole();
                    if (user.hasUsePorte()) {
                        playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                        return;
                    }
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownHuitieme() <= 0) {
                            playerClick.setMaxHealth(playerClick.getMaxHealth() + (5D * 2D));
                            if ((playerClick.getMaxHealth() - playerClick.getHealth()) < (5D * 2D)) {
                                playerClick.setHealth(playerClick.getMaxHealth());
                            } else {
                                playerClick.setHealth(playerClick.getHealth() + (5D * 2D));
                            }

                            if (playerClick.hasPotionEffect(PotionEffectType.SPEED))
                                playerClick.removePotionEffect(PotionEffectType.SPEED);
                            if (playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                                playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            if (playerClick.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                                playerClick.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                            playerClick.addPotionEffect(
                                    new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 2, 0, false, false));
                            playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 2, 1, false, false));
                            playerClick.addPotionEffect(
                                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 2, 0, false, false));
                            playerClick.addPotionEffect(
                                    new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * 2, 0, false, false));
                            new DoubleCircleEffect(20 * 2 * 60, EnumParticle.FLAME).start(playerClick);
                            PlayerManager.setRoleCooldownHuitieme(20 * 5 * 60);
                            PlayerManager.setUseHuitimePorte(true);
                            playerClick.setItemInHand(new GaiNuitItem().toItemStack());
                            user.setHasUsePorte(true);
                            new BukkitRunnable() {

                                int timer = 2 * 60;

                                @Override
                                public void run() {
                                    Player player = playerClick;
                                    if (Bukkit.getPlayer(playerClick.getUniqueId()) != null)
                                        player = Bukkit.getPlayer(playerClick.getUniqueId());
                                    PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
                                    if (PlayerManager.isAlive() && !(UhcHost.getInstance().getNarutoManager().isWaittingRessurct(player.getUniqueId()))) {
                                        ActionBar.sendMessage(player, "§aTemps restant : §e" + new FormatTime(timer).toString());
                                        if (timer == 0) {
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 60 * 15, 0, false, false));
                                            player.setMaxHealth(2D * 6D);
                                            PlayerManager.setUseHuitimePorte(false);
                                            cancel();
                                        }
                                        timer--;
                                    } else {
                                        timer = 2 * 60;
                                        PlayerManager.setUseHuitimePorte(false);
                                        cancel();
                                    }


                                }
                            }.runTaskTimer(UhcHost.getInstance(), 0, 20);
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownHuitieme()));
                        }
                    } else {
                        playerClick.sendMessage(
                                Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }
            }

        });
    }


}
