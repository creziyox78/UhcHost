package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Choji;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BouletHumainItem extends QuickItem {

    private final UhcHost main = UhcHost.getInstance();

    public BouletHumainItem() {
        super(Material.NETHER_STAR);
        super.setName("§bBoulet Humain");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (!main.getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                    if (PlayerManager.getRole() instanceof Choji) {
                        Choji choji = (Choji) PlayerManager.getRole();
                        if (PlayerManager.getRoleCooldownBouletHumain() <= 0) {
                            if (playerClick.hasPotionEffect(PotionEffectType.SPEED)) {
                                playerClick.removePotionEffect(PotionEffectType.SPEED);
                            }
                            playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1, false, false));
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            choji.usePower(PlayerManager);
                            PlayerManager.setRoleCooldownBouletHumain(60 * 15);
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownBouletHumain(), playerClick.getItemInHand());
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownBouletHumain()));
                        }
                    } else {
                        playerClick.sendMessage(Messages.not("Choji"));
                    }
                } else {
                    playerClick.sendMessage(
                            Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                }
            }
        });
    }

}
