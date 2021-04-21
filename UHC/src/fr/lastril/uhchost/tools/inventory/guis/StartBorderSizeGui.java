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
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import fr.lastril.uhchost.tools.inventory.LargeTimerGui;

public class StartBorderSizeGui extends LargeTimerGui {

	public StartBorderSizeGui(Player player) {
		super(player, I18n.tl("guis.startBorderSize.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.STAINED_GLASS_PANE,
				"§a" + (UhcHost.getInstance()).worldBorderUtils.getStartSize(),
				Arrays.asList(I18n.tl("guis.startBorderSize.lore", new String[0]),
						I18n.tl("guis.startBorderSize.lore1", new String[0]),
						I18n.tl("guis.startBorderSize.lore2", new String[0])),
				1, (byte) 13);
		inventory.setItem(4, ic.create());
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
			case STAINED_GLASS_PANE:
				this.player.closeInventory();
				(new BorderGui(this.player)).show();
				break;
			case BANNER:
				name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
				value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(name);
				if (value < 1)
					break;
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);
				ic = new ItemsCreator(Material.STAINED_GLASS_PANE,
						"§a" + (UhcHost.getInstance()).worldBorderUtils.getStartSize(),
						Arrays.asList(I18n.tl("guis.startBorderSize.lore", new String[0]),
								I18n.tl("guis.startBorderSize.lore1", new String[0]),
								I18n.tl("guis.startBorderSize.lore2", new String[0])),
						1, (byte) 13);
				inventory.setItem(4, ic.create());
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
