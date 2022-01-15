package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.soulsociety.SoiFon;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Suzumebachi extends QuickItem {

    public Suzumebachi(UhcHost main) {
        super(Material.DOUBLE_PLANT, 1, (byte)2);
        super.setName("§6Suzumebachi");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(playerManager.getRole() instanceof SoiFon){
                    if(playerManager.getRoleCooldownSuzumebachi() >= 0){
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSuzumebachi()));
                    }
                }
            }
        });
    }
}
