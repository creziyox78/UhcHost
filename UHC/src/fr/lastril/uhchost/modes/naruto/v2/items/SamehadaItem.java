package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class SamehadaItem extends QuickItem {
	
	public SamehadaItem() {
		super(Material.DIAMOND_SWORD);
		super.setName("§cSamehada");
		super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
	}

}
