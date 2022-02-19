package fr.lastril.uhchost.modes.tpg.kits;

import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class AerienKit extends KitTaupe {
    public AerienKit() {
        super("Aérien");
        super.addItem(new QuickItem(Material.ENDER_PEARL, 16).toItemStack());
        super.addItem(new Livre(Enchantment.PROTECTION_FALL, 4).toItemStack());
        super.addItem(new Livre(Enchantment.KNOCKBACK, 2).toItemStack());
        super.addItem(new Livre(Enchantment.ARROW_KNOCKBACK, 2).toItemStack());
    }
}
