package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Gaara;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShukakuItem extends QuickItem {

    public ShukakuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cShukaku");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Gaara) {
                    Gaara gaara = (Gaara) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownShukaku() <= 0) {
                            gaara.setInShukaku(true);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes sous Shukaku");
                            PlayerManager.setRoleCooldownShukaku(20 * 60);
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    gaara.setInShukaku(false);
                                }
                            }.runTaskLater(main, 20 * 60 * 5);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownShukaku()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Nagato"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
