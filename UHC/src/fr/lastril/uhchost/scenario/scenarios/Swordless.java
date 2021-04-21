package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;

public class Swordless extends Scenario {

	public Swordless() {
		super("Swordless", Arrays.asList(I18n.tl("scenarios.swordless", new String[0])), Material.DIAMOND_SWORD);
	}

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		if (event.getInventory().getResult().getType() == Material.DIAMOND_SWORD
				|| event.getInventory().getResult().getType() == Material.GOLD_SWORD
				|| event.getInventory().getResult().getType() == Material.IRON_SWORD
				|| event.getInventory().getResult().getType() == Material.STONE_SWORD
				|| event.getInventory().getResult().getType() == Material.WOOD_SWORD)
			event.getInventory().setResult((new ItemsCreator(Material.AIR, null, null)).create());
	}

}
