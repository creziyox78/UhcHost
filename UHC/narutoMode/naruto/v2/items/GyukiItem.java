package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GyukiItem extends QuickItem {

    private final int time = 60 * 5 * 20;

    public GyukiItem() {
        super(Material.NETHER_STAR);
        super.setName("§eGyûki");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof KillerBee) {
                    if (PlayerManager.getRoleCooldownGyuki() <= 0) {
                        KillerBee killerBee = (KillerBee) PlayerManager.getRole();
                        killerBee.usePower(PlayerManager);
                        playerClick.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, 0, false, false));
                        playerClick.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, 3, false, false));
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        PlayerManager.setRoleCooldownGyuki(20 * 60);
                        PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownGyuki(), playerClick.getItemInHand());
                    } else {
                        playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownGyuki()));
                    }
                }
            }
        });
    }

}
