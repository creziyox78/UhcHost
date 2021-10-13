package fr.lastril.uhchost.tools.API;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class Livre {
	
	private final ItemStack item;
	private final EnchantmentStorageMeta meta;
	
	public Livre(Enchantment enchantement, int power) {
		this.item = new ItemStack(Material.ENCHANTED_BOOK);
		this.meta = (EnchantmentStorageMeta) this.item.getItemMeta();
		this.meta.addStoredEnchant(enchantement, power, true);
	}
	
	public Livre addEnchantement(Enchantment enchantement, int power) {
		this.meta.addStoredEnchant(enchantement, power, true);
		return this;
	}
	
	public ItemStack toItemStack(){
		this.item.setItemMeta(meta);
		return this.item;
	}
	
}
