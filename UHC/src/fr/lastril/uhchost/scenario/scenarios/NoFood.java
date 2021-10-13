package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Arrays;

public class NoFood extends Scenario {
	
	public NoFood() {
		super("NoFood", Arrays.asList(I18n.tl("scenarios.nofood.lore", ""), ""), Material.BREAD);
	}
	
	@EventHandler
	public void onNoFood(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
		e.setCancelled(true);
	}

}
