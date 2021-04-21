package fr.lastril.uhchost.player.events.normal;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.team.TeamsGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.inventory.Gui;
import fr.lastril.uhchost.tools.inventory.NotStart;
import fr.lastril.uhchost.tools.inventory.guis.HostConfig;

public class Interact implements Listener {
	private NotStart notstart;

	public Interact(NotStart notstart) {
		this.notstart = notstart;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack current = e.getItem();
		GameManager gameManager = UhcHost.getInstance().getGamemanager();
		if (current == null)
			return;
		if (!current.hasItemMeta())
			return;
		if (!current.getItemMeta().hasDisplayName())
			return;
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Devenir Host"))
			if (player.hasPermission("uhc.host")) {
				if (gameManager.getHost() == null) {
					gameManager.setHostname(player.getName());
					gameManager.setHost(player);
					this.notstart.PreHost(player);
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 20.0F);
					ActionBar.sendMessage(player, I18n.tl("became-host", new String[0]));
				} else {
					ActionBar.sendMessage(player, I18n.tl("already-host", new String[0]));
				}
			} else {
				ActionBar.sendMessage(player, I18n.tl("no-permission", new String[0]));
			}
		
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Ne plus être Host")) {
			
			ActionBar.sendMessage(player, I18n.tl("not-became-host", new String[0]));
			gameManager.setHost(null);
			gameManager.setHostname(null);
			this.notstart.PreHost(player);
			player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 0.6F);
		}
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Lobby"))
			if (UhcHost.getInstance().getConfig().getBoolean("bungeecord")) {
				BungeeAPI.ConnectBungeeServer(player,
						UhcHost.getInstance().getConfig().getString("server-redirection"));
				player.sendMessage(ChatColor.YELLOW + "Lobby...");
			} else {
				player.kickPlayer(ChatColor.RED + "Disconnected");
			}
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Configuration")) {
			player.closeInventory();
			player.openInventory(HostConfig.Main());
			Gui.inventory = HostConfig.Main();
		}
		
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("teams", new String[0]))) {
			new TeamsGui(player);
		}
	}
}
