package fr.lastril.uhchost.inventory.guis.nether;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class NetherGui extends Gui {

	public NetherGui(Player player) {
		super(player, 9, I18n.tl("guis.nether.name"));
		ItemsCreator ic = new ItemsCreator(Material.OBSIDIAN, I18n.tl("guis.nether.time"),
				Arrays.asList(I18n.tl("guis.nether.timeLore")), 1, (byte) 13);
		inventory.setItem(3, ic.create());
		if ((UhcHost.getInstance()).gameManager.isNether()) {
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOn"),
					Arrays.asList(I18n.tl("guis.nether.stateOnLore")), 1, (byte) 13);
		} else {
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOff"),
					Arrays.asList(I18n.tl("guis.nether.stateOffLore")), -1, (byte) 14);
		}
		inventory.setItem(4, ic.create());
		ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null);
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
				new HostConfig(player).show();
				break;
			case STAINED_GLASS_PANE:
				(UhcHost.getInstance()).gameManager.setNether(!(UhcHost.getInstance()).gameManager.isNether());
				if ((UhcHost.getInstance()).gameManager.isNether()) {
					ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOn"),
							Arrays.asList(I18n.tl("guis.nether.stateOnLore")), 1,
							(byte) 13);
				} else {
					ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOff"),
							Arrays.asList(I18n.tl("guis.nether.stateOffLore")), -1,
							(byte) 14);
				}
				inventory.setItem(4, ic.create());
				break;
			case OBSIDIAN:
				this.player.closeInventory();
				new NetherEndTimeGui(this.player).show();
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}

}
