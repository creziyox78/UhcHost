package fr.lastril.uhchost.tools.inventory.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.Items;
import net.md_5.bungee.api.ChatColor;

public class Settings {
	public static Inventory rulesSettings(ItemStack current) {
		Inventory inv = Bukkit.createInventory(null, 45, ChatColor.AQUA + "Settings");
		inv.setItem(4, current);
		inv.setItem(11, Items.getItemColored(Material.WOOL, ChatColor.GREEN + "+1", (byte) 5, true));
		inv.setItem(20, Items.getItemColored(Material.WOOL, ChatColor.GREEN + "+5", (byte) 5, true));
		inv.setItem(29, Items.getItemColored(Material.WOOL, ChatColor.GREEN + "+10", (byte) 5, true));
		inv.setItem(15, Items.getItemColored(Material.WOOL, ChatColor.RED + "-1", (byte) 14, true));
		inv.setItem(24, Items.getItemColored(Material.WOOL, ChatColor.RED + "-5", (byte) 14, true));
		inv.setItem(33, Items.getItemColored(Material.WOOL, ChatColor.RED + "-10", (byte) 14, true));
		inv.setItem(44, Items.getItem(Material.ARROW, I18n.tl("guis.back", ""), false));
		return inv;
	}

	public static Inventory borderSettings(ItemStack current) {
		Inventory inv = Bukkit.createInventory(null, 45, ChatColor.AQUA + "Settings");
		inv.setItem(4, current);
		inv.setItem(11, Items.getItemColored(Material.WOOL, ChatColor.GREEN + "+10", (byte) 5, true));
		inv.setItem(20, Items.getItemColored(Material.WOOL, ChatColor.GREEN + "+50", (byte) 5, true));
		inv.setItem(29, Items.getItemColored(Material.WOOL, ChatColor.GREEN + "+100", (byte) 5, true));
		inv.setItem(15, Items.getItemColored(Material.WOOL, ChatColor.RED + "-10", (byte) 14, true));
		inv.setItem(24, Items.getItemColored(Material.WOOL, ChatColor.RED + "-50", (byte) 14, true));
		inv.setItem(33, Items.getItemColored(Material.WOOL, ChatColor.RED + "-100", (byte) 14, true));
		inv.setItem(44, Items.getItem(Material.ARROW, I18n.tl("guis.back", ""), false));
		return inv;
	}
}
