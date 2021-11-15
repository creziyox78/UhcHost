package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import java.util.Arrays;

public class Rodless extends Scenario {

	public Rodless() {
		super("Rodless", Arrays.asList(I18n.tl("scenarios.rodless")),
				Material.COOKED_FISH);
	}

	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		if (event.getInventory().getResult().getType() == Material.FISHING_ROD)
			event.getInventory().setResult((new ItemsCreator(Material.AIR, null, null)).create());
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntityType() == EntityType.EGG || event.getEntityType() == EntityType.SNOWBALL)
			event.setCancelled(true);
	}

}
