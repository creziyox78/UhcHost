package fr.lastril.uhchost.tools.creators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemsCreator {

	private Material m;

	private String name;

	private List<String> lores;

	private Map<Enchantment, Integer> enchants;

	private int amount;

	private byte data;

	public ItemsCreator(Material m, String name, List<String> lores, int amount, byte data) {
		if (amount == 0)
			amount++;
		if (data == 0)
			data = (byte) (data + 1);
		this.m = m;
		this.name = name;
		this.lores = lores;
		this.amount = amount;
		this.data = data;
		this.enchants = new HashMap<>();
	}

	public ItemsCreator(Material m, String name, List<String> lores, int amount) {
		this.m = m;
		this.name = name;
		this.lores = lores;
		this.amount = amount;
		this.data = 0;
	}

	public ItemsCreator(Material m, String name, List<String> lores) {
		this.m = m;
		this.name = name;
		this.lores = lores;
		this.amount = 1;
		this.data = 0;
	}

	public void addEnchantment(Enchantment enchant, int level) {
		if (this.enchants != null) {
			this.enchants.put(enchant, Integer.valueOf(level));
		} else {
			this.enchants = new HashMap<>();
			this.enchants.put(enchant, Integer.valueOf(level));
		}
	}

	public ItemStack create() {
		ItemStack is = new ItemStack(this.m, this.amount, (short) this.data);
		ItemMeta meta = is.getItemMeta();
		if (this.name != null)
			meta.setDisplayName(this.name);
		if (this.lores != null)
			meta.setLore(this.lores);
		is.setItemMeta(meta);
		if (this.enchants != null)
			for (Map.Entry<Enchantment, Integer> entry : this.enchants.entrySet()) {
				if (((Integer) entry.getValue()).intValue() == 250) {
					is.addUnsafeEnchantment(entry.getKey(), ((Integer) entry.getValue()).intValue());
					continue;
				}
				is.addEnchantment(entry.getKey(), ((Integer) entry.getValue()).intValue());
			}
		return is;
	}

	public ItemStack createHead(String owner) {
		ItemStack is = new ItemStack(Material.SKULL_ITEM, this.amount, (short) 3);
		SkullMeta meta = (SkullMeta) is.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(this.name);
		if (this.lores != null)
			meta.setLore(this.lores);
		is.setItemMeta((ItemMeta) meta);
		return is;
	}
	
	

	public ItemStack createHead(UUID owner) {
		ItemStack is = new ItemStack(Material.SKULL_ITEM, this.amount, (short) 3);
		SkullMeta meta = (SkullMeta) is.getItemMeta();
		meta.setOwner(Bukkit.getOfflinePlayer(owner).getName());
		meta.setDisplayName(this.name);
		if (this.lores != null)
			meta.setLore(this.lores);
		is.setItemMeta((ItemMeta) meta);
		return is;
	}

	public Material getMaterial() {
		return this.m;
	}

	public void setMaterial(Material m) {
		this.m = m;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLores() {
		return this.lores;
	}

	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	public Map<Enchantment, Integer> getEnchants() {
		return this.enchants;
	}

	public void setEnchants(Map<Enchantment, Integer> enchants) {
		this.enchants = enchants;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public byte getData() {
		return this.data;
	}

	public void setData(byte data) {
		this.data = data;
	}

}
