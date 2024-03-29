package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import java.util.Arrays;

public class NoBow extends Scenario {

	public NoBow() {
		super("NoBow", Arrays.asList(I18n.tl("scenarios.nobow")), Material.BOW);
	}

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		if (event.getInventory().getResult().getType() == Material.BOW)
			event.getInventory().setResult((new ItemsCreator(Material.AIR, null, null)).create());
	}

}
