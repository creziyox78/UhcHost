package fr.lastril.uhchost.modes.naruto.v2.items.swords;

import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class SusanoSword extends QuickItem {

    public SusanoSword() {
        super(Material.DIAMOND_SWORD);
        super.setName("§bSusano");
        super.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
    }
}
