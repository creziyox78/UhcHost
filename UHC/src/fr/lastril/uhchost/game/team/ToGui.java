package fr.lastril.uhchost.game.team;

import java.util.Arrays;

import org.bukkit.DyeColor;
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
import fr.lastril.uhchost.tools.creators.BannerCreator;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import fr.lastril.uhchost.tools.inventory.Gui;
import fr.lastril.uhchost.tools.inventory.guis.HostConfig;

public class ToGui extends Gui {

	private int[] possibilities = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 50 };

	public ToGui(Player player) {
		super(player, 9, I18n.tl("guis.teamsOf.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.BLAZE_ROD,
				"§e" + (UhcHost.getInstance()).teamUtils.getPlayersPerTeams(),
				Arrays.asList(I18n.tl("guis.teamsOf.lore", new String[0])));
		inventory.setItem(4, ic.create());
		BannerCreator bc = new BannerCreator(I18n.tl("guis.timer.previous", new String[0]), Arrays.asList(""), 1, true);
		bc.setBaseColor(DyeColor.RED);
		inventory.setItem(0, bc.create());
		bc = new BannerCreator("§aSuivant", Arrays.asList(""), 1, true);
		bc.setBaseColor(DyeColor.GREEN);
		inventory.setItem(8, bc.create());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().equals(inventory)) {
			String name;
			ItemsCreator ic;
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			switch (is.getType()) {
			case BLAZE_ROD:
				this.player.closeInventory();
				HostConfig.Main();
				break;
			case BANNER:
				name = is.getItemMeta().getDisplayName();
				if (name.equalsIgnoreCase(I18n.tl("guis.timer.previous", new String[0]))) {
					for (int i = 0; i < this.possibilities.length; i++) {
						if (this.possibilities[i] == (UhcHost.getInstance()).teamUtils.getPlayersPerTeams()) {
							if (i <= 0) {
								(UhcHost.getInstance()).teamUtils
										.setPlayersPerTeams(this.possibilities[this.possibilities.length - 1]);
								break;
							}
							(UhcHost.getInstance()).teamUtils.setPlayersPerTeams(this.possibilities[i - 1]);
							break;
						}
					}
				} else if (name.equalsIgnoreCase(I18n.tl("guis.timer.next", new String[0]))) {
					for (int i = 0; i < this.possibilities.length; i++) {
						if (this.possibilities[i] == (UhcHost.getInstance()).teamUtils.getPlayersPerTeams()) {
							if (i + 1 >= this.possibilities.length) {
								(UhcHost.getInstance()).teamUtils.setPlayersPerTeams(this.possibilities[0]);
								break;
							}
							(UhcHost.getInstance()).teamUtils.setPlayersPerTeams(this.possibilities[i + 1]);
							break;
						}
					}
				}
				if ((UhcHost.getInstance()).teamUtils.getPlayersPerTeams() == 1) {
					(UhcHost.getInstance()).teamUtils.resetTeams();
				} else {
					(UhcHost.getInstance()).teamUtils.setupTeams();
				}
				ic = new ItemsCreator(Material.BLAZE_ROD, "§e" + (UhcHost.getInstance()).teamUtils.getPlayersPerTeams(),
						Arrays.asList("Nombre de joueurs par teams"));
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
