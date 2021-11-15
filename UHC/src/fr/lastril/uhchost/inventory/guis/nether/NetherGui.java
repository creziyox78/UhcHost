package fr.lastril.uhchost.inventory.guis.nether;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Arrays;

public class NetherGui extends IQuickInventory {

	public NetherGui() {
		super(I18n.tl("guis.nether.name"), 9);

	}


	@Override
	public void contents(QuickInventory inv) {
		ItemsCreator ic = new ItemsCreator(Material.OBSIDIAN, I18n.tl("guis.nether.time"),
				Arrays.asList(I18n.tl("guis.nether.timeLore")), 1, (byte) 13);
		inv.setItem(ic.create(), onClick -> {
			new NetherEndTimeGui().open(onClick.getPlayer());
		},3);

		if ((UhcHost.getInstance()).gameManager.isNether()) {
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOn"),
					Arrays.asList(I18n.tl("guis.nether.stateOnLore")), 1, (byte) 13);
		} else {
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.nether.stateOff"),
					Arrays.asList(I18n.tl("guis.nether.stateOffLore")), -1, (byte) 14);
		}
		inv.setItem(ic.create(), onClick -> {
			(UhcHost.getInstance()).gameManager.setNether(!(UhcHost.getInstance()).gameManager.isNether());
		},4);
		ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null);
		inv.setItem(ic.create(), onClick -> {
			new HostConfig().open(onClick.getPlayer());
		},5);
	}
}
