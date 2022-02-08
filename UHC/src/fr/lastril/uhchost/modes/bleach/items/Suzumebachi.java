package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.SoiFon;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Suzumebachi extends QuickItem {

    public Suzumebachi(UhcHost main) {
        super(Material.DOUBLE_PLANT, 1, (byte)2);
        super.setName("§6Suzumebachi");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRole() instanceof SoiFon){
                        if(playerManager.getRoleCooldownSuzumebachi() >= 0){
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSuzumebachi()));
                        } else {

                        }
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }

            }
        });
    }
}
