package fr.lastril.uhchost.modes.naruto.v2.items.swords;

import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class YariSword extends QuickItem {

    public YariSword() {
        super(Material.DIAMOND_SWORD);
        super.setName("§cYari");
        super.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
        super.setDurability(Material.DIAMOND_SWORD.getMaxDurability());
    }
}
