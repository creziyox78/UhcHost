package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;

public class NoBow extends Scenario {

	public NoBow() {
		super("Bowless", Arrays.asList(I18n.tl("scenarios.nobow", new String[0])), Material.BOW);
	}

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		if (event.getInventory().getResult().getType() == Material.BOW)
			event.getInventory().setResult((new ItemsCreator(Material.AIR, null, null)).create());
	}

}
