package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public class NoEnchant extends Scenario {

	public NoEnchant() {
		super("No Enchant", Arrays.asList(I18n.tl("scenarios.noenchant.lore"),
				I18n.tl("scenarios.noenchant.lore1")), Material.BOOK);
	}

	@EventHandler
	public void onIventoryOpen(InventoryOpenEvent event) {
		if (event.getInventory().getType() == InventoryType.ENCHANTING
				|| event.getInventory().getType() == InventoryType.ANVIL) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(I18n.tl("scenarios.noenchant.message"));
		}
	}

}
