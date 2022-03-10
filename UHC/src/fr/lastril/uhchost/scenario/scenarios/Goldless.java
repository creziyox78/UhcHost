package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.inventory.guis.scenarios.GoldlessGui;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;

public class Goldless extends Scenario {

	private static int lootNumber = 1;

	public Goldless() {
		super("Goldless", Arrays.asList(I18n.tl("scenarios.goldless.lore"), I18n.tl("scenarios.goldless.lore1")),
				Material.GOLD_ORE, GoldlessGui.class);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.getDrops().add((new ItemsCreator(Material.GOLD_ORE, null, null, lootNumber)).create());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() == Material.GOLD_ORE) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(I18n.tl("scenarios.goldless.message"));
		}
	}

	public static int getLootNumber() {
		return lootNumber;
	}

	public static void setLootNumber(int lootNumber) {
		Goldless.lootNumber = lootNumber;
	}

}
