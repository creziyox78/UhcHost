package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.api.particles.DoubleCircleEffect;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.maygo.uhc.obj.PlayerManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GaiNuitItem extends QuickItem {

    public GaiNuitItem() {
        super(Material.NETHER_STAR);
        super.setName("§4Gaï de la Nuit");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (PlayerManager.getRole() instanceof GaiMaito) {
                    GaiMaito gaiMaito = (GaiMaito) PlayerManager.getRole();
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.isUseHuitimePorte()) {
                            playerClick.setItemInHand(null);
                            gaiMaito.setInGaiNuit(true);
                            if (playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                                playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            if (playerClick.hasPotionEffect(PotionEffectType.SPEED))
                                playerClick.removePotionEffect(PotionEffectType.SPEED);
                            playerClick.addPotionEffect(
                                    new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 2, false, false));
                            playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2, false, false));
                            new DoubleCircleEffect(20 * 10, EnumParticle.REDSTONE).start(playerClick);

                            new BukkitRunnable() {

                                int timer = 10;

                                @Override
                                public void run() {
                                    PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
                                    if (PlayerManager.isAlive() && !(UhcHost.getInstance().getNarutoManager().isWaittingRessurct(playerClick.getUniqueId()))) {
                                        ActionBar.sendMessage(playerClick, "§aTemps restant : §e" + new FormatTime(timer).toString());
                                        if (timer == 0) {
                                            gaiMaito.setInGaiNuit(false);
                                            if (gaiMaito.isMustDie()) playerClick.damage(100);
                                            cancel();
                                        }
                                        timer--;
                                    } else {
                                        timer = 2 * 60;
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(UhcHost.getInstance(), 0, 20);
                        } else {
                            playerClick.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre item après avoir utiliser la Huitième Porte."));
                        }
                    } else {
                        playerClick.sendMessage(
                                Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    playerClick.sendMessage(Messages.not("Gaï Maito"));
                }
            }
        });
    }
}
