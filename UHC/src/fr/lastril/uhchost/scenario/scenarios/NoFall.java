package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;

public class NoFall extends Scenario {
	
	public NoFall() {
		super("NoFall", Arrays.asList(I18n.tl("scenarios.nofall.lore", "")), Material.CHAINMAIL_BOOTS);
	}
	
	@EventHandler
	public void NoFallDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL) {
			e.setDamage(0);
			e.setCancelled(true);
		}
	}

}
