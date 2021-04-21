
package fr.lastril.uhchost.tools.inventory.guis;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;

public class HostConfig {
	public static Inventory Main() {
		Inventory inv = Bukkit.createInventory(null, 45, ChatColor.GOLD + "Configuration");
		for (int i = 0; i < 45; i++) {
			inv.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 8, true));
		}
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
		}
		for (int i = 9; i < 37; i += 9) {
			inv.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
		}
		for (int i = 17; i < 36; i += 9) {
			inv.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
		}

		if (!GameState.isState(GameState.STARTING)) {
			inv.setItem(40, Items.getItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "Lancer la partie", true));
		} else {
			inv.setItem(40, Items.getItem(Material.REDSTONE_BLOCK, ChatColor.RED + "Annuler le lancement", true));
		}
		inv.setItem(11, Items.getItem(Material.PAPER, ChatColor.AQUA + "Modes de jeu", true));
		inv.setItem(13, Items.getItem(Material.PAPER, ChatColor.AQUA + "Règles", true));
		inv.setItem(15, Items.getItem(Material.NAME_TAG, ChatColor.AQUA + "Scenarios", true));
		inv.setItem(44, Items.customItem(new ItemStack(Material.EMERALD), ChatColor.WHITE + "Whitelist",
				Arrays.asList(ChatColor.GRAY + "Status: " + ChatColor.RED + "OFF"), null));
		if (Bukkit.hasWhitelist())
			inv.setItem(44, Items.customItem(new ItemStack(Material.REDSTONE), ChatColor.WHITE + "Whitelist",
					Arrays.asList(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "ON"), null));
		
		
		ItemsCreator ic = new ItemsCreator(Material.SKULL_ITEM, I18n.tl("guis.main.maxPlayers", new String[0]), Arrays.asList(I18n.tl("guis.main.maxPlayersLore", new String[0])));
	    inv.setItem(19, ic.createHead("MHF_Golem"));
		
		return inv;
	}

	public static Inventory Rules() {
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Règles");
		ItemStack ic = new ItemsCreator(Material.BLAZE_ROD, I18n.tl("guis.main.teams", new String[0]),
				Arrays.asList(I18n.tl("guis.main.teamsLore", new String[0]))).create();
		inv.setItem(0, ic);
		ic = new ItemsCreator(Material.DIAMOND_SWORD, I18n.tl("guis.main.pvp", new String[0]), Arrays
				.asList(I18n.tl("guis.main.pvpLore", new String[0]), I18n.tl("guis.main.pvpLore1", new String[0])))
						.create();
		inv.setItem(3, ic);
		ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.main.border", new String[0]),
				Arrays.asList(I18n.tl("guis.main.borderLore", new String[0]))).create();
		inv.setItem(5, ic);
		ic = new ItemsCreator(Material.OBSIDIAN, I18n.tl("guis.main.nether", new String[0]), Arrays.asList(I18n.tl("guis.main.netherLore", new String[0]))).create();
	    inv.setItem(8, ic);
	    
		inv.setItem(18, Items.getItem(Material.CHEST, ChatColor.YELLOW + "Inventaire de départ", true));
		inv.setItem(26, Items.getItem(Material.ARROW, I18n.tl("guis.back", ""), false));
		return inv;
	}
}