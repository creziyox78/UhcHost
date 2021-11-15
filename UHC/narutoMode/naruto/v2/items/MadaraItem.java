package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Madara;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MadaraItem extends QuickItem {

    public MadaraItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dMadara");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Madara) {
                        if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {

                            boolean lost = false;

                            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                                lost = true;
                                player.removePotionEffect(PotionEffectType.SPEED);
                            } else {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                            }

                            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                                lost = true;
                                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            } else {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                            }


                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez " + (lost ? "perdu" : "reçu") + " vos effets.");
                        } else {
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.not("Madara"));
                        return;
                    }
                } else {
                    player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                    return;
                }
            }

        });
    }

}
