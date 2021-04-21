package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class NoEnchant extends Scenario {

	public NoEnchant() {
		super("No Enchant", Arrays.asList(I18n.tl("scenarios.noenchant.lore", new String[0]),
				I18n.tl("scenarios.noenchant.lore1", new String[0])), Material.BOOK);
	}

	@EventHandler
	public void onIventoryOpen(InventoryOpenEvent event) {
		if (event.getInventory().getType() == InventoryType.ENCHANTING
				|| event.getInventory().getType() == InventoryType.ANVIL) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(I18n.tl("scenarios.noenchant.message", new String[0]));
		}
	}

}
