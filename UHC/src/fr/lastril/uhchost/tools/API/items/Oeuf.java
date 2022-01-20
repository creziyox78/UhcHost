package fr.lastril.uhchost.tools.API.items;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

public class Oeuf {
	
	private final SpawnEgg egg;

	public Oeuf(EntityType type) {
		this.egg = new SpawnEgg(type);
	}
	
	public ItemStack toItemStack(int amount) {
		return egg.toItemStack(amount);
	}
	
}
