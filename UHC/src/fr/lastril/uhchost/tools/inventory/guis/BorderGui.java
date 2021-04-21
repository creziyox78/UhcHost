package fr.lastril.uhchost.tools.inventory.guis;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import fr.lastril.uhchost.tools.inventory.Gui;

public class BorderGui extends Gui {

	public BorderGui(Player player) {
		super(player, 18, I18n.tl("guis.border.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.STAINED_GLASS_PANE,
				I18n.tl("guis.border.initialSize", new String[0]),
				Arrays.asList(I18n.tl("guis.border.initialSizeLore", new String[0])), 1, (byte) 13);
		inventory.setItem(3, ic.create());
		ic = new ItemsCreator(Material.NETHER_STAR, I18n.tl("guis.border.speed", new String[0]),
				Arrays.asList(I18n.tl("guis.border.speedLore", new String[0])));
		inventory.setItem(4, ic.create());
		ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.border.finalSize", new String[0]),
				Arrays.asList(I18n.tl("guis.border.finalSizeLore", new String[0])), 1, (byte) 14);
		inventory.setItem(5, ic.create());
		ic = new ItemsCreator(Material.COBBLE_WALL, I18n.tl("guis.border.time", new String[0]),
				Arrays.asList(I18n.tl("guis.border.timeLore", new String[0])));
		inventory.setItem(13, ic.create());
		inventory.setItem(17, (new ItemsCreator(Material.BARRIER, I18n.tl("guis.back", new String[0]), null)).create());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().equals(inventory)) {
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			if (is.getType() == Material.STAINED_GLASS_PANE && is.getDurability() == 13) {
				event.getWhoClicked().closeInventory();
				(new StartBorderSizeGui(this.player)).show();
			} else if (is.getType() == Material.NETHER_STAR) {
				event.getWhoClicked().closeInventory();
				(new BorderSpeedGui(this.player)).show();
			} else if (is.getType() == Material.STAINED_GLASS_PANE && is.getDurability() == 14) {
				event.getWhoClicked().closeInventory();
				(new FinalBorderSizeGui(this.player)).show();
			} else if (is.getType() == Material.COBBLE_WALL) {
				event.getWhoClicked().closeInventory();
				(new BorderTimeGui(this.player)).show();
			} else if (is.getType() == Material.BARRIER) {
				event.getWhoClicked().closeInventory();
				HostConfig.Rules();
			}
		}
	}

	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll((Listener) this);
	}

}
