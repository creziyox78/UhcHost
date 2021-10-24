package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ActiveScenariosGui extends Gui {

	public ActiveScenariosGui(Player player) {
		super(player, 54, I18n.tl("guis.activescenarios.name", ""));
		for (Scenario scenario : UhcHost.getInstance().getGamemanager().getScenarios()) {
			if (scenario.getData() != 0) {
				inventory.addItem(new ItemStack(new ItemsCreator(scenario.getType(), scenario.getName(),
						scenario.getDescritpion(), 1, scenario.getData()).create()));
				continue;
			}
			inventory.addItem(new ItemStack(new ItemsCreator(scenario.getType(), scenario.getName(), scenario.getDescritpion()).create()));
		}
		if(UhcHost.getInstance().getGamemanager().getScenarios().isEmpty()) {
			inventory.setItem(22,new ItemStack(new ItemsCreator(Material.BARRIER, I18n.tl("guis.activescenarios.noscenario", ""), Arrays.asList("")).create()));
		}
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
			if (is.getType() == Material.BARRIER)
				event.getWhoClicked().closeInventory();
		}
	}

	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}

}
