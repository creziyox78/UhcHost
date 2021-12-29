package fr.lastril.uhchost.modes.naruto.v2.items.swords;


import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class GunbaiSword extends QuickItem {

    public GunbaiSword() {
        super(Material.DIAMOND_SWORD);
        super.setName("§bGunbai");
        super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
    }
}
