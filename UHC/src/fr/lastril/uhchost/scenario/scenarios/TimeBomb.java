package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.scenarios.TimeBombGui;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.runnable.TimeBombRunnable;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TimeBomb extends Scenario {

	private static int count = 30;

	public TimeBomb() {
		super("Time Bomb",
				Arrays.asList(I18n.tl("scenarios.timebomb.lore"),
						I18n.tl("scenarios.timebomb.lore1"),
						I18n.tl("scenarios.timebomb.lore2", String.valueOf(TimeBomb.getCount()))),
				Material.TNT, TimeBombGui.class);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.getEntity().getLocation().getBlock().setType(Material.CHEST);
		event.getEntity().getLocation().getBlock().getRelative(BlockFace.EAST).setType(Material.CHEST);
		Chest chest = (Chest) event.getEntity().getLocation().getBlock().getState();
		for (ItemStack itemStack : event.getDrops()) {
			if (itemStack == null)
				continue;
			chest.getInventory().addItem(itemStack);
		}
		chest.update();
		event.getDrops().clear();
		(new TimeBombRunnable(chest, TimeBomb.getCount())).runTaskTimer(UhcHost.getInstance(), 0L, 20L);
	}

	public static int getCount() {
		return count;
	}

	public static int setCount(int count) {
		return TimeBomb.count = count;
	}

}
