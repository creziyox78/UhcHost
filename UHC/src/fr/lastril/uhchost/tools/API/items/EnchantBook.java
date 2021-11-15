package fr.lastril.uhchost.tools.API.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnchantBook {
	public static ItemStack SetUnbreakable(ItemStack hand, boolean unbreakable) {
		ItemMeta handM = hand.getItemMeta();
		handM.spigot().setUnbreakable(unbreakable);
		hand.setItemMeta(handM);
		return hand;
	}

	public static ItemStack AddEnchantment(ItemStack hand, Enchantment enchant) {
		ItemMeta handM = hand.getItemMeta();
		handM.addEnchant(enchant, hand.getEnchantmentLevel(enchant) + 1, true);
		if (hand.getEnchantmentLevel(enchant) >= 255)
			handM.removeEnchant(enchant);
		hand.setItemMeta(handM);
		return hand;
	}

	public static ItemStack RemoveEnchantment(ItemStack hand, Enchantment enchant) {
		ItemMeta handM = hand.getItemMeta();
		handM.addEnchant(enchant, hand.getEnchantmentLevel(enchant) - 1, true);
		if (hand.getEnchantmentLevel(enchant) <= 1)
			handM.removeEnchant(enchant);
		hand.setItemMeta(handM);
		return hand;
	}

	public static ItemStack Unbreakable(boolean status) {
		ItemStack customItem = new ItemStack(Material.BEDROCK, 1);
		ItemMeta customMItem = customItem.getItemMeta();
		customMItem.spigot().setUnbreakable(true);
		customMItem.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		if (status) {
			customMItem.setDisplayName(ChatColor.AQUA + "Unbreakable: " + ChatColor.GREEN + "enabled");
		} else {
			customMItem.setDisplayName(ChatColor.AQUA + "Unbreakable: " + ChatColor.RED + "disabled");
		}
		customMItem.setLore(Arrays.asList(ChatColor.GOLD+"Clique ici afin de mettre l'item incassable ou non."));
		customItem.setItemMeta(customMItem);
		return customItem;
	}

	public static ItemStack Book(String options, int level) {
		ItemStack customItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta customMItem = customItem.getItemMeta();
		if (options == "Protection")
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		if (options == "Fire Protection") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Feather Falling") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Blast Protection") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Projectile Protection") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Respiration") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Aqua Affinity") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Thorns") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Depth Strider") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Sharpness") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Smite") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Bane of Arthropods") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Knockback") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Fire Aspect") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Looting") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Power") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Punch") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Flame") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Infinity") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Efficiency") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Silk Touch") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Unbreaking") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Fortune") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Luck of the Sea") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		} else if (options == "Lure") {
			customMItem.setDisplayName(ChatColor.WHITE + options + ": " + level);
		}
		customItem.setAmount(level);
		customMItem.spigot().setUnbreakable(true);
		customMItem.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		customMItem.setLore(Arrays.asList("", ChatColor.GREEN + "Clique Gauche: Ajouter un niveau d'enchantement",
				ChatColor.RED + "Clique Droit: Retirer un niveau d'enchantement"));
		customItem.setItemMeta(customMItem);
		return customItem;
	}
}
