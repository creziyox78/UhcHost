package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Itegumo extends QuickItem {
    public Itegumo(UhcHost main) {
        super(Material.GLOWSTONE_DUST);
        super.setName("§9Itegumo");
        super.setLore("",
                "§7Octroie§c Force 1§7 pendant 20 secondes.",
                "§750% des dégâts infligé à un joueur sont",
                "§7donné à l'allié le plus bas en vie",
                "§7dans un rayon de 10 blocs.",
                "§7(Cooldown - 5 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());

        });
    }
}
