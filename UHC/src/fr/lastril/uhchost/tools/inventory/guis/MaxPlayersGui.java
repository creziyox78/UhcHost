package fr.lastril.uhchost.tools.inventory.guis;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.scenario.gui.TimerGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;

public class MaxPlayersGui extends TimerGui {

	public MaxPlayersGui(Player player) {
		super(player, I18n.tl("guis.maxPlayers.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.SKULL_ITEM,
				"§e" + (UhcHost.getInstance()).gameManager.getMaxPlayers(),
				Arrays.asList(I18n.tl("guis.maxPlayers.lore", new String[0])));
		inventory.setItem(4, ic.createHead("MHF_Golem"));
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().equals(inventory)) {
			String name;
			int value;
			ItemsCreator ic;
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			switch (is.getType()) {
			case SKULL_ITEM:
				this.player.closeInventory();
				player.openInventory(HostConfig.Main());
				break;
			case BANNER:
				name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
				value = (UhcHost.getInstance()).gameManager.getMaxPlayers() + Integer.parseInt(name);
				if (value < 2)
					break;
				(UhcHost.getInstance()).gameManager.setMaxPlayers(value);
				ic = new ItemsCreator(Material.SKULL_ITEM, "§e" + (UhcHost.getInstance()).gameManager.getMaxPlayers(),
						Arrays.asList(new String[] { I18n.tl("guis.maxPlayers.lore", new String[0]) }));
				inventory.setItem(4, ic.createHead("MHF_Golem"));
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll((Listener) this);
	}

}
