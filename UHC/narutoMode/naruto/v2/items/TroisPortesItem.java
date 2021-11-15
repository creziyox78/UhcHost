package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.naruto.v2.roles.HuitPorteUser;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.RockLee;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TroisPortesItem extends QuickItem {

    public TroisPortesItem() {
        super(Material.NETHER_STAR);
        super.setName("§2Trois portes");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (isRightRole(PlayerManager.getRole())) {
                    HuitPorteUser user = (HuitPorteUser) PlayerManager.getRole();
                    if (user.hasUsePorte()) {
                        playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                        return;
                    }
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownTroisPortes() <= 0) {
                            if (playerClick.getHealth() <= playerClick.getHealth() - 2D) {
                                playerClick.damage(100);
                            } else {
                                playerClick.setHealth(playerClick.getHealth() - 2D);
                                if (playerClick.hasPotionEffect(PotionEffectType.SPEED))
                                    playerClick.removePotionEffect(PotionEffectType.SPEED);
                                playerClick.addPotionEffect(
                                        new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 2, 0, false, false));
                                PlayerManager.setRoleCooldownTroisPortes(10);
                            }

                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownTroisPortes()));
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
