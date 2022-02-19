package fr.lastril.uhchost.modes.tpg.kits;

import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class PyromaneKit extends KitTaupe {
    public PyromaneKit() {
        super("Pyromane");
        super.addItem(new Livre(Enchantment.ARROW_FIRE, 1).toItemStack());
        super.addItem(new QuickItem(Material.LAVA_BUCKET, 2).toItemStack());
        super.addItem(new QuickItem(Material.FLINT_AND_STEEL, 1).toItemStack());
    }
}
