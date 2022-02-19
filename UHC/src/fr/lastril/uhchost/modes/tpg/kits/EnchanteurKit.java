package fr.lastril.uhchost.modes.tpg.kits;

import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class EnchanteurKit extends KitTaupe {
    public EnchanteurKit() {
        super("Enchanteur");
        super.addItem(new Livre(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        super.addItem(new Livre(Enchantment.DAMAGE_ALL, 1).toItemStack());
        super.addItem(new QuickItem(Material.BOOK, 6).toItemStack());
    }
}
