package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReviveItem extends QuickItem {
    public ReviveItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§aRevive");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof KillerBee) {
                    KillerBee killerBee = (KillerBee) PlayerManager.getRole();
                    killerBee.setFakeDeath(false);
                    main.getInvisibleTeam().removeEntry(player.getName());
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.showPlayer(player);
                    }
                    player.setItemInHand(null);
                } else {
                    player.sendMessage(Messages.not("Killer Bee"));
                }
            }
        });
    }
}
