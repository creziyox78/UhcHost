package fr.lastril.uhchost.scenario.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.scenario.scenarios.BowSwap;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BowSwapGui extends TimerGui {

	public BowSwapGui(Player player) {
		super(player, I18n.tl("guis.bowswap.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.SNOW_BALL, "§e" + BowSwap.getTpPercentage(),
				Arrays.asList(I18n.tl("guis.bowswap.lore", new String[0])));
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
			case SNOW_BALL:
				this.player.closeInventory();
				(UhcHost.getInstance()).getGamemanager().removeScenario(Scenarios.BOWSWAP.getScenario());
				Scenarios.BOWSWAP.setScenario((Scenario) new BowSwap());
				(UhcHost.getInstance()).getGamemanager().addScenario(Scenarios.BOWSWAP.getScenario());
				(new ScenariosGui(this.player)).show();
				break;
			case BANNER:
				name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
				value = BowSwap.getTpPercentage() + Integer.parseInt(name);
				if (value < 0 || value > 100)
					break;
				BowSwap.setTpPercentage(value);
				ic = new ItemsCreator(Material.SNOW_BALL, "§e" + BowSwap.getTpPercentage(),
						Arrays.asList(I18n.tl("guis.bowswap.lore", new String[0])));
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
