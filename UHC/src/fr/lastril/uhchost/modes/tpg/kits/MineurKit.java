package fr.lastril.uhchost.modes.tpg.kits;

import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class MineurKit extends KitTaupe {
    public MineurKit() {
        super("Mineur");
        super.addItem(new QuickItem(Material.FURNACE, 4).toItemStack());
        super.addItem(new QuickItem(Material.COAL, 16).toItemStack());
        super.addItem(new QuickItem(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 3 , true).toItemStack());
    }
}
