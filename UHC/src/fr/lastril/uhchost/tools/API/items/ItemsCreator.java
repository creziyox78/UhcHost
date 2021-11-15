package fr.lastril.uhchost.tools.API.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
		if (amount == 0)
			amount++;
		this.m = m;
		this.name = name;
		this.lores = lores;
		this.amount = amount;
	}

	public ItemsCreator(Material m, String name, List<String> lores) {
		if (amount == 0)
			amount++;
		this.m = m;
		this.name = name;
		this.lores = lores;
	}

	public ItemsCreator addEnchantment(Enchantment enchant, int level) {
		if (this.enchants != null) {
			this.enchants.put(enchant, Integer.valueOf(level));
		} else {
			this.enchants = new HashMap<>();
			this.enchants.put(enchant, Integer.valueOf(level));
		}
		return this;
	}

	public ItemStack create() {
		ItemStack is = new ItemStack(this.m, this.amount, this.data);
		ItemMeta meta = is.getItemMeta();
		if (this.name != null)
			meta.setDisplayName(this.name);
		if (this.lores != null)
			meta.setLore(this.lores);
		is.setItemMeta(meta);
		if (this.enchants != null)
			for (Map.Entry<Enchantment, Integer> entry : this.enchants.entrySet()) {
				if (entry.getValue().intValue() == 250) {
					is.addUnsafeEnchantment(entry.getKey(), entry.getValue().intValue());
					continue;
				}
				is.addEnchantment(entry.getKey(), entry.getValue().intValue());
			}
		return is;
	}

	public ItemStack createHead(String owner) {
		ItemStack is = new ItemStack(Material.SKULL_ITEM, this.amount, (byte) 3);
		SkullMeta meta = (SkullMeta) is.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(this.name);
		if (this.lores != null)
			meta.setLore(this.lores);
		is.setItemMeta(meta);
		return is;
	}
	
	

	public ItemStack createHead(UUID owner) {
		ItemStack is = new ItemStack(Material.SKULL_ITEM, this.amount, (short) 3);
		SkullMeta meta = (SkullMeta) is.getItemMeta();
		meta.setOwner(Bukkit.getOfflinePlayer(owner).getName());
		meta.setDisplayName(this.name);
		if (this.lores != null)
			meta.setLore(this.lores);
		is.setItemMeta(meta);
		return is;
	}

	public Material getMaterial() {
		return this.m;
	}

	public ItemsCreator setMaterial(Material m) {
		this.m = m;
		return this;
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

	public ItemsCreator setLores(List<String> lores) {
		this.lores = lores;
		return this;
	}

	public Map<Enchantment, Integer> getEnchants() {
		return this.enchants;
	}

	public ItemsCreator setEnchants(Map<Enchantment, Integer> enchants) {
		this.enchants = enchants;
		return this;
	}

	public int getAmount() {
		return this.amount;
	}

	public ItemsCreator setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public byte getData() {
		return this.data;
	}

	public ItemsCreator setData(byte data) {
		this.data = data;
		return this;
	}

}
