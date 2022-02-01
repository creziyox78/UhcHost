package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Byakuya;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Senbonzakura extends QuickItem {
    public Senbonzakura(UhcHost main) {
        super(Material.INK_SACK);
        super.setName("§dSenbonzakura");
        super.setLore("",
                "§7Créer une zone (30x30) qui se",
                "§7dissipe au bout de 3 minutes.",
                "§7");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Byakuya){
                Byakuya byakuya = (Byakuya) playerManager.getRole();
                if(playerManager.getRoleCooldownSenbonzakura() <= 0){
                    byakuya.createZone(player.getLocation());
                }
            }
        });
    }


}
