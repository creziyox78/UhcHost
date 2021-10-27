package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.inventory.guis.items.PotionsGui;
import fr.lastril.uhchost.team.TeamsGui;
import fr.lastril.uhchost.tools.NotStart;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import java.util.ListIterator;

public class Interact implements Listener {

	private final NotStart notstart;
	private final UhcHost pl;

	public Interact(NotStart notstart, UhcHost pl) {
		this.notstart = notstart;
		this.pl = pl;
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
					ActionBar.sendMessage(player, I18n.tl("became-host"));
				} else if(UhcHost.getInstance().gameManager.isCoHost(player)) {
					this.notstart.PreHost(player);
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 20.0F);
				} else {
					ActionBar.sendMessage(player, I18n.tl("already-host"));
				}

			} else {
				ActionBar.sendMessage(player, I18n.tl("no-permission"));
			}
		
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Ne plus être Host")) {
			
			ActionBar.sendMessage(player, I18n.tl("not-became-host"));
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
			new HostConfig(player).show();
		}

		if (this.pl.gameManager.isPotionsEditMode()) {
			for (ListIterator<ItemStack> listIterator = player.getInventory().iterator(); listIterator.hasNext(); ) {
				ItemStack itemStack = listIterator.next();
				if (itemStack != null &&
						itemStack.getType() == Material.POTION &&
						!this.pl.gameManager.getDeniedPotions().contains(Potion.fromItemStack(itemStack)))
					this.pl.gameManager.getDeniedPotions().add(Potion.fromItemStack(itemStack));
			}
			this.notstart.PreHost(player);
			this.pl.gameManager.setPotionsEditMode(false);
			new PotionsGui(player).show();
		}
		if (current.getItemMeta().getDisplayName().equalsIgnoreCase(I18n.tl("teams"))) {
			new TeamsGui(player);
		}
	}
}
