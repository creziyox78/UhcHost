package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class WabisukeFishin extends QuickItem {
    public WabisukeFishin(UhcHost main) {
        super(Material.FISHING_ROD);
        super.setName("§6Wabisuke");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.setInfinityDurability();
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Permet d'attirer le joueur attrapé",
                "§7tout en lui retirant§c 2 coeurs non-permanents.",
                "§7(Cooldown - 30 secondes)");
    }
}
