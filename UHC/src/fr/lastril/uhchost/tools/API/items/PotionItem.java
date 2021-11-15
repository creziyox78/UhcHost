package fr.lastril.uhchost.tools.API.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class PotionItem {
	
	private final Potion potion;

	public PotionItem(PotionType type) {
		this(type, 1);
	}
	
	public PotionItem(PotionType type, int level) {
		this(type, level, false);
	}
	
	public PotionItem(PotionType type, int level, boolean splash) {
		Potion potion = new Potion(type, level);
		potion.setSplash(splash);
		this.potion = potion;
	}
	
	public ItemStack toItemStack(int amount) {
		return potion.toItemStack(amount);
	}
	
}
