package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class BleedingSweets extends Scenario {

	public BleedingSweets() {
		super("Bleeding Sweets",
				Arrays.asList(I18n.tl("scenarios.bleedingsweets.lore"), 
						I18n.tl("scenarios.bleedingsweets.lore1"),
					I18n.tl("scenarios.bleedingsweets.lore2")
					),
				Material.ARROW);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		e.getDrops().add(new ItemStack(Material.DIAMOND));
		e.getDrops().add(new ItemStack(Material.STRING));
		e.getDrops().add(new ItemStack(Material.GOLD_INGOT, 5));
		e.getDrops().add(new ItemStack(Material.ARROW, 16));
	}

}
