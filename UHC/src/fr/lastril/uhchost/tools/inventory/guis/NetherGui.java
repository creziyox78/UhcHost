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

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import fr.lastril.uhchost.tools.inventory.Gui;

public class NetherGui extends Gui {

	public NetherGui(Player player) {
		super(player, 9, I18n.tl("guis.nether.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.OBSIDIAN, I18n.tl("guis.nether.time", new String[0]),
				Arrays.asList(new String[] { I18n.tl("guis.nether.timeLore", new String[0]) }), 1, (byte) 13);
		inventory.setItem(3, ic.create());
		if ((UhcHost.getInstance()).gameManager.isNether()) {
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOn", new String[0]),
					Arrays.asList(new String[] { I18n.tl("guis.nether.stateOnLore", new String[0]) }), 1, (byte) 13);
		} else {
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOff", new String[0]),
					Arrays.asList(new String[] { I18n.tl("guis.nether.stateOffLore", new String[0]) }), -1, (byte) 14);
		}
		inventory.setItem(4, ic.create());
		ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back", new String[0]), null);
		inventory.setItem(5, ic.create());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().equals(inventory)) {
			ItemsCreator ic;
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			switch (is.getType()) {
			case BARRIER:
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().openInventory(HostConfig.Rules());
				break;
			case STAINED_GLASS_PANE:
				(UhcHost.getInstance()).gameManager.setNether(!(UhcHost.getInstance()).gameManager.isNether());
				if ((UhcHost.getInstance()).gameManager.isNether()) {
					ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOn", new String[0]),
							Arrays.asList(I18n.tl("guis.nether.stateOnLore", new String[0])), 1,
							(byte) 13);
				} else {
					ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOff", new String[0]),
							Arrays.asList(I18n.tl("guis.nether.stateOffLore", new String[0])), -1,
							(byte) 14);
				}
				inventory.setItem(4, ic.create());
				break;
			case OBSIDIAN:
				this.player.closeInventory();
				(new NetherEndTimeGui(this.player)).show();
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
