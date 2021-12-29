package fr.lastril.uhchost.modes.naruto.v2.items.swords;

import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class KubikiribochoSword extends QuickItem {
	
	public KubikiribochoSword() {
		super(Material.DIAMOND_SWORD);
		super.setName("§3Kubikiribôchô");
		super.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
	}

}
