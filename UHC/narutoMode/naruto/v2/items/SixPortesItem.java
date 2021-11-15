package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.api.particles.DoubleCircleEffect;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.naruto.v2.roles.HuitPorteUser;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.RockLee;
import fr.maygo.uhc.obj.PlayerManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SixPortesItem extends QuickItem {

    public SixPortesItem() {
        super(Material.NETHER_STAR);
        super.setName("§2Six portes");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_BLOCK || onClick.getAction() == Action.RIGHT_CLICK_AIR) {
                if (isRightRole(PlayerManager.getRole())) {
                    HuitPorteUser user = (HuitPorteUser) PlayerManager.getRole();
                    if (user.hasUsePorte()) {
                        playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                        return;
                    }
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(onClick.getPlayer().getUniqueId())) {
                        if (PlayerManager.getRoleCooldownSixPortes() <= 0) {
                            if (playerClick.getMaxHealth() <= playerClick.getMaxHealth() - 2D) {
                                playerClick.damage(100);
                            } else {
                                playerClick.setMaxHealth(playerClick.getMaxHealth() - 2D);
                                if (playerClick.hasPotionEffect(PotionEffectType.SPEED))
                                    playerClick.removePotionEffect(PotionEffectType.SPEED);
                                if (playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                                    playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                                playerClick.addPotionEffect(
                                        new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 0, false, false));
                                playerClick.addPotionEffect(
                                        new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 0, false, false));
                                new DoubleCircleEffect(20 * 2 * 60, EnumParticle.VILLAGER_HAPPY).start(playerClick);
                                PlayerManager.setRoleCooldownSixPortes(10);
                            }
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownSixPortes()));
                        }

                    } else {
                        playerClick
                                .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }
            }

        });
    }

    private boolean isRightRole(Role role) {
        return role instanceof RockLee || role instanceof GaiMaito;
    }


}
