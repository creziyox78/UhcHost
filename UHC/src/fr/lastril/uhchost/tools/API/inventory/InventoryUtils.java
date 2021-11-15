package fr.lastril.uhchost.tools.API.inventory;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    private final UhcHost main;

    public InventoryUtils(UhcHost main) {
        this.main = main;
    }

    public void dropInventory(Location loc, ItemStack[] items, ItemStack[] armors) {
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                loc.getWorld().dropItem(loc, item);
            }
        }
        for (ItemStack armor : armors) {
            if (armor != null && armor.getType() != Material.AIR) {
                loc.getWorld().dropItem(loc, armor);
            }
        }
    }

    public void giveItemSafely(Player player, ItemStack itemStack){
        if(!hasInventoryFull(player)){
            player.getInventory().addItem(itemStack);
        }else{
            player.getWorld().dropItemNaturally(player.getLocation().clone().add(0, 1, 0), itemStack);
            player.sendMessage("§cVotre inventaire est pleins, votre item a été jeté par terre.");
        }
    }

    public boolean hasInventoryFull(Player player){
        return player.getInventory().firstEmpty() == -1;
    }

}
