package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Tsunade;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ByakugoItem extends QuickItem {

    public ByakugoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bByakugô");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.getRole() instanceof Tsunade) {
                Tsunade tsunade = (Tsunade) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                    if (PlayerManager.getRoleCooldownByakugo() == 0) {
                        if (tsunade.isUseByakugo()) {
                            playerClick.sendMessage(Messages.error("Vous utilisez déjà votre Byakugo, attendez que son effet se dissipe."));
                            return;
                        }
                        if (playerClick.hasPotionEffect(PotionEffectType.REGENERATION)) {
                            playerClick.removePotionEffect(PotionEffectType.REGENERATION);
                        }
                        playerClick.addPotionEffect(
                                new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false));
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        tsunade.setUseByakugo(true);
                    } else {
                        playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownByakugo()));
                    }
                } else {
                    playerClick
                            .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                }
            }
        });
    }
}
