package fr.lastril.uhchost.tools;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Location;
import org.bukkit.Material;
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
}
