package fr.lastril.uhchost.tools;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

public class NotStart {
	

	public void PreHost(Player player) {
		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		player.getInventory().setBoots(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setHelmet(null);
		player.setTotalExperience(0);
		player.setExp(0);
		player.getInventory().clear();
		player.setMaxHealth(20.0D);
		player.setHealth(20.0D);
		if ((UhcHost.getInstance()).teamUtils.getPlayersPerTeams() != 1) {
			ItemStack ic = new ItemsCreator(Material.BANNER, "Equipes", null).create();
			player.getInventory().setItem(0, ic);
		}
		if (player == UhcHost.getInstance().getGamemanager().getHost()) {
			player.getInventory().setItem(4, Items.getItem(Material.CHEST, ChatColor.GOLD + "Configuration", true));
			player.getInventory().setItem(7, Items.getItem(Material.REDSTONE, ChatColor.GOLD + "Ne plus être Host", true));
		} else {
			player.getInventory().setItem(7, Items.getItem(Material.DIAMOND, ChatColor.AQUA + "Devenir Host", true));
		}
		player.getInventory().setItem(8, Items.getItem(Material.BED, ChatColor.RED + "Lobby", true));
	}

	public static void PreHosting(Player player) {
		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		player.getInventory().setBoots(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setHelmet(null);
		player.setTotalExperience(0);
		player.setExp(0);
		player.getInventory().clear();
		player.setMaxHealth(20.0D);
		player.setHealth(20.0D);
		player.setGameMode(GameMode.ADVENTURE);

		if ((UhcHost.getInstance()).teamUtils.getPlayersPerTeams() != 1) {
			ItemStack ic = new ItemsCreator(Material.BANNER, "Equipes", null).create();
			player.getInventory().setItem(0, ic);
		}
		if (player == UhcHost.getInstance().getGamemanager().getHost()) {
			player.getInventory().setItem(4, Items.getItem(Material.CHEST, ChatColor.GOLD + "Configuration", true));
			player.getInventory().setItem(7, Items.getItem(Material.REDSTONE, ChatColor.GOLD +  "Ne plus être Host", true));
		} else {
			player.getInventory().setItem(7, Items.getItem(Material.DIAMOND, ChatColor.AQUA + "Devenir Host", true));
		}
		player.getInventory().setItem(8, Items.getItem(Material.BED, ChatColor.RED + "Lobby", true));
	}

	public static void checkingWorld(Player player) {
		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		player.getInventory().setBoots(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setHelmet(null);
		player.setExp(0.0F);
		player.getInventory().clear();
		player.setMaxHealth(20.0D);
		player.setHealth(20.0D);
		player.setGameMode(GameMode.CREATIVE);
		player.getInventory().setItem(3, new ItemsCreator(Material.INK_SACK,
				"§cSupprimer le monde", Arrays.asList("§cNécéssite un redémarrage !"), 1, (byte)1).create());
		player.getInventory().setItem(5, new ItemsCreator(Material.INK_SACK,
				"§aValider le monde", Arrays.asList("§aVous pourrez prégénérer le monde."), 1, (byte)10).create());


	}
}
