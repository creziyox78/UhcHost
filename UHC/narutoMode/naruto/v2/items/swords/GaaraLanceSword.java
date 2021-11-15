package fr.lastril.uhchost.modes.naruto.v2.items.swords;

import fr.atlantis.api.item.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class GaaraLanceSword extends QuickItem {

    public GaaraLanceSword() {
        super(Material.DIAMOND_SWORD);
        super.setName("§cLance");
        super.setDurability((short) (Material.DIAMOND_SWORD.getMaxDurability() - 25));
        super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
    }
}
