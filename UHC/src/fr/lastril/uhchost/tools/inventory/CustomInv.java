package fr.lastril.uhchost.tools.inventory;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomInv {
	public static HashMap<String, ItemStack[]> inventoryContents = new HashMap<>();

	public static ItemStack helmet;

	public static ItemStack chestplate;

	public static ItemStack leggings;

	public static ItemStack boots;

	public static void saveInventory(Player player) {
		helmet = player.getInventory().getHelmet();
		chestplate = player.getInventory().getChestplate();
		leggings = player.getInventory().getLeggings();
		boots = player.getInventory().getBoots();
		inventoryContents.put("start", player.getInventory().getContents());
		player.getInventory().clear();
	}

	public static void restoreInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setContents(inventoryContents.get("start"));
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
	}

	public static void createInventory() {
		inventoryContents.put("start", new ItemStack[0]);
		helmet = new ItemStack(Material.AIR);
		chestplate = new ItemStack(Material.AIR);
		leggings = new ItemStack(Material.AIR);
		boots = new ItemStack(Material.AIR);
	}
}
