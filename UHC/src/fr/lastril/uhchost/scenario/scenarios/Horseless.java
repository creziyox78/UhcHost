package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Arrays;

public class Horseless extends Scenario {
	
	public Horseless() {
		super("Horseless", Arrays.asList(I18n.tl("scenarios.horseless.lore", "")), Material.SADDLE);
	}
	
	@EventHandler
	public void onEntityMount(EntityMountEvent event) {
		if (event.getEntity() != null && event.getEntity() instanceof Player && event.getMount() != null && event.getMount() instanceof Horse) {
			event.setCancelled(true);
	    }
	}

}
