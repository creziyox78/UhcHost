package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Mayuri;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Shikai extends QuickItem {
    public Shikai(UhcHost main) {
        super(Material.SPIDER_EYE);
        super.setName("§5Shikai");
        super.setLore("",
                "§7Donne§2 Poison 1§7 à chaque coup",
                "§7pendant 5 secondes. Dure 1 minute.",
                "§7(Cooldown - 5 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.isAlive() && playerManager.hasRole()){
                if(playerManager.getRole() instanceof Mayuri){
                    Mayuri mayuri = (Mayuri) playerManager.getRole();
                    if(bleachPlayerManager.canUsePower()){
                        if(playerManager.getRoleCooldownShikai() <= 0){
                            mayuri.setInShikai(true);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownShikai()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.not("Mayuri"));
                }
            }
        });
    }
}
