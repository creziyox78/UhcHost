package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.gui.DiamondlessGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;

public class Diamondless extends Scenario {

	private static int lootNumber = 1;

	public Diamondless() {
		super("Diamondless", Arrays.asList(I18n.tl("scenarios.diamondless.lore", new String[0]),
				I18n.tl("scenarios.diamondless.lore1", new String[0])), Material.DIAMOND, DiamondlessGui.class);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.getDrops().add((new ItemsCreator(Material.DIAMOND, null, null, lootNumber)).create());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() == Material.DIAMOND_ORE) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(I18n.tl("scenarios.diamondless.message", new String[0]));
		}
	}

	public static int getLootNumber() {
		return lootNumber;
	}

	public static void setLootNumber(int lootNumber) {
		Diamondless.lootNumber = lootNumber;
	}

}
