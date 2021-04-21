package fr.lastril.uhchost.tools.inventory.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.inventory.EnchantBook;

public class Enchant {
	public static Inventory Categories(Player player, ItemStack item) {
		try {
			Inventory Categories = Bukkit.createInventory(null, 45, ChatColor.LIGHT_PURPLE + "Categories");
			Categories.setItem(3, item);
			if (item.getItemMeta().spigot().isUnbreakable()) {
				Categories.setItem(5, EnchantBook.Unbreakable(true));
			} else {
				Categories.setItem(5, EnchantBook.Unbreakable(false));
			}
			Categories.setItem(19, Items.getItem(Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE + "Sword", true));
			Categories.setItem(21, Items.getItem(Material.DIAMOND_CHESTPLATE, ChatColor.LIGHT_PURPLE + "Armor", true));
			Categories.setItem(23, Items.getItem(Material.BOW, ChatColor.LIGHT_PURPLE + "Bow", true));
			Categories.setItem(25, Items.getItem(Material.DIAMOND_PICKAXE, ChatColor.LIGHT_PURPLE + "Tool", true));
			Categories.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Close", (byte) 1, true));
			return Categories;
		} catch (Exception e) {
			player.sendMessage("Error in Categories menu !");
			return null;
		}
	}

	public static Inventory Sword(Player player, ItemStack item) {
		try {
			Inventory Sword = Bukkit.createInventory(null, 45, ChatColor.LIGHT_PURPLE + "Sword");
			Sword.setItem(4, item);
			Sword.setItem(19, EnchantBook.Book("Sharpness", item.getEnchantmentLevel(Enchantment.DAMAGE_ALL)));
			Sword.setItem(20, EnchantBook.Book("Smite", item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)));
			Sword.setItem(21, EnchantBook.Book("Bane of Arthropods", item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)));
			Sword.setItem(23, EnchantBook.Book("Knockback", item.getEnchantmentLevel(Enchantment.KNOCKBACK)));
			Sword.setItem(24, EnchantBook.Book("Fire Aspect", item.getEnchantmentLevel(Enchantment.FIRE_ASPECT)));
			Sword.setItem(25, EnchantBook.Book("Looting", item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)));
			Sword.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
			Sword.setItem(31, EnchantBook.Book("Unbreaking", item.getEnchantmentLevel(Enchantment.DURABILITY)));
			return Sword;
		} catch (Exception e) {
			player.sendMessage("dans le menu Ep!");
			return null;
		}
	}

	public static Inventory Armor(Player player, ItemStack item) {
		try {
			Inventory Armor = Bukkit.createInventory(null, 45, ChatColor.LIGHT_PURPLE + "Armor");
			Armor.setItem(4, item);
			Armor.setItem(18,
					EnchantBook.Book("Protection", item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)));
			Armor.setItem(19,
					EnchantBook.Book("Fire Protection", item.getEnchantmentLevel(Enchantment.PROTECTION_FIRE)));
			Armor.setItem(20,
					EnchantBook.Book("Feather Falling", item.getEnchantmentLevel(Enchantment.PROTECTION_FALL)));
			Armor.setItem(21,
					EnchantBook.Book("Blast Protection", item.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS)));
			Armor.setItem(22, EnchantBook.Book("Projectile Protection",
					item.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)));
			Armor.setItem(23, EnchantBook.Book("Respiration", item.getEnchantmentLevel(Enchantment.OXYGEN)));
			Armor.setItem(24, EnchantBook.Book("Aqua Affinity", item.getEnchantmentLevel(Enchantment.WATER_WORKER)));
			Armor.setItem(25, EnchantBook.Book("Thorns", item.getEnchantmentLevel(Enchantment.THORNS)));
			Armor.setItem(26, EnchantBook.Book("Depth Strider", item.getEnchantmentLevel(Enchantment.DEPTH_STRIDER)));
			Armor.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
			Armor.setItem(31, EnchantBook.Book("Unbreaking", item.getEnchantmentLevel(Enchantment.DURABILITY)));
			return Armor;
		} catch (Exception e) {
			player.sendMessage("dans le menu Armure !");
			return null;
		}
	}

	public static Inventory Bow(Player player, ItemStack item) {
		try {
			Inventory Bow = Bukkit.createInventory(null, 45, ChatColor.LIGHT_PURPLE + "Bow");
			Bow.setItem(4, item);
			Bow.setItem(20, EnchantBook.Book("Power", item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)));
			Bow.setItem(21, EnchantBook.Book("Punch", item.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK)));
			Bow.setItem(23, EnchantBook.Book("Flame", item.getEnchantmentLevel(Enchantment.ARROW_FIRE)));
			Bow.setItem(24, EnchantBook.Book("Infinity", item.getEnchantmentLevel(Enchantment.ARROW_INFINITE)));
			Bow.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
			Bow.setItem(31, EnchantBook.Book("Unbreaking", item.getEnchantmentLevel(Enchantment.DURABILITY)));
			return Bow;
		} catch (Exception e) {
			player.sendMessage("dans le menu Arc !");
			return null;
		}
	}

	public static Inventory Tool(Player player, ItemStack item) {
		try {
			Inventory Tool = Bukkit.createInventory(null, 45, ChatColor.LIGHT_PURPLE + "Tool");
			Tool.setItem(4, item);
			Tool.setItem(19, EnchantBook.Book("Efficiency", item.getEnchantmentLevel(Enchantment.DIG_SPEED)));
			Tool.setItem(20, EnchantBook.Book("Silk Touch", item.getEnchantmentLevel(Enchantment.SILK_TOUCH)));
			Tool.setItem(22, EnchantBook.Book("Fortune", item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)));
			Tool.setItem(24, EnchantBook.Book("Luck of the Sea", item.getEnchantmentLevel(Enchantment.LUCK)));
			Tool.setItem(25, EnchantBook.Book("Lure", item.getEnchantmentLevel(Enchantment.LURE)));
			Tool.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
			Tool.setItem(31, EnchantBook.Book("Unbreaking", item.getEnchantmentLevel(Enchantment.DURABILITY)));
			return Tool;
		} catch (Exception e) {
			player.sendMessage("dans le menu Outil !");
			return null;
		}
	}
}
