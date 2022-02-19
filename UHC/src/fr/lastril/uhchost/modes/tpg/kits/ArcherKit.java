package fr.lastril.uhchost.modes.tpg.kits;

import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class ArcherKit extends KitTaupe {
    public ArcherKit() {
        super("Archer");
        super.addItem(new QuickItem(Material.ARROW, 64).toItemStack());
        super.addItem(new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3, true).toItemStack());
    }
}
