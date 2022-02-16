package fr.lastril.uhchost.modes.lg.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class CoupleBoussoleItem extends QuickItem {
    public CoupleBoussoleItem(UhcHost main) {
        super(Material.COMPASS);
        super.setName("§dBoussole Magique");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
            if(wolfPlayerManager.isInCouple()){
                Player couple = Bukkit.getPlayer(wolfPlayerManager.getOtherCouple());
                if(couple != null){
                    player.setCompassTarget(couple.getLocation());
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLa boussole pointe vers la position de votre amoureux. Cliquez à nouveau pour actualiser la position.");
                } else {
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aCette boussole pointe vers le centre.");
                }
            }
        });
    }
}
