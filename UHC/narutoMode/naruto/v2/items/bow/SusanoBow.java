package fr.lastril.uhchost.modes.naruto.v2.items.bow;

import fr.atlantis.api.item.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class SusanoBow extends QuickItem {

    public SusanoBow() {
        super(Material.BOW);
        super.setName("§bSusano");
        super.addEnchant(Enchantment.ARROW_DAMAGE, 7, true);
    }

}
